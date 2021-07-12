#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <semaphore.h>

sem_t semaphores[24];
int currentTime = 0;	//Global time counting up
int currentCar = 0;		//Keeps track of the number of cars that have passed through, starting with car0
int carCount = 0;		//Overall crossing count, no matter the direction
int bufferCar;

sem_t headOfLine;	//Determines which car gets to go next after having stopped at the stop sign

//Each car contains 3 properties
struct Car {
	int index;		//General arrival order
	int arriveTime;	//Specific arrival time
	int dir;		//Specific direction (original & target direction combined)
};

//8 cars are initialized for travel through the intersection (Add more if you want to test edge cases)
struct Car cars[8] = {
	{0, 11, 7},
	{1, 20, 7},
	{2, 33, 8},
	{3, 35, 1},
	{4, 42, 2},
	{5, 44, 7},
	{6, 57, 11},
	{7, 59, 3}
};

//All 24 Semaphores organized in a double array, based on what semaphores a car will need in order to cross the street
/*	-1 [indices 2 - 5] signifies its a right turn and only needs two locks out the 6 to cross
	0 [index 6] signifies the number of cars currently following this path across the street (no cars at initialization)*/
int reqSems[12][7] = {
	{18, 19, -1, -1, -1, -1,		0},		//SW 0  Right		(0 % 3 = 0, +1 = 1 == 10 seconds)	
	{18, 8, 9, 10, 11, 21,			0},		//SS 1  Straight	(1 % 3 = 1, +1 = 2 == 20 seconds)
	{18, 7, 1, 0, 15, 23,			0},		//SE 2  Left		(2 % 3 = 2, +1 = 3 == 30 seconds)

	{16, 17, -1, -1, -1, -1,		0},		//WN 3  Right
	{16, 5, 6, 7, 8, 19,			0},		//WW 4  Straight
	{16, 4, 0, 3, 12, 21,			0},		//WS 5  Left

	{22, 23, -1, -1, -1, -1,		0},		//NE 6  Right
	{22, 14, 15, 4, 5, 17,			0},		//NN 7  Straight
	{22, 13, 3, 2, 9, 19,			0},		//NW 8  Left

	{20, 21, -1, -1, -1, -1,		0},		//ES 9  Right
	{20, 11, 12, 13, 14, 23,		0},		//EE 10 Straight
	{20, 10, 2, 1, 6, 17,			0}		//EN 11 Left
};

//S = 21, W = 19, N = 17, E = 23
//Used in message displays for the original direction of a car (char intToOD(int val)...)
char dirs[4] = { 'S', 'W', 'N', 'E' };

//Both "intToOD" (Original Direction) and "intToTD" (Target Direction) are used exclusively in the "DisplayCar" function
char intToOD(int val) { return dirs[val / 3]; }

char intToTD(int val) {
	int fDir = (reqSems[val][5] < 0) ? reqSems[val][1] : reqSems[val][5];
	switch (fDir) {
	case 21: return dirs[0];
	case 19: return dirs[1];
	case 17: return dirs[2];
	case 23: return dirs[3];
	}
}

//Displays message every time a car thread enters the "arrive", "cross", and "exit" functions
void DisplayCar(struct Car car) {			//	Time in seconds,		Car ID,		Original dir,		Target dir	
	printf("Time %.1f: Car %d (->%c ->%c) ", ((float)currentTime)/10.0 , car.index, intToOD(car.dir), intToTD(car.dir));
}

//"arrive" function
void ArriveIntersection(struct Car car) {
	DisplayCar(car); printf("arriving\n");
	carCount++;											
	sem_wait(&headOfLine);								//Only one car can be primed to cross, no exceptions
	if (reqSems[car.dir][6] == 0) {						/*But once the headOfLine is empty, it is possible to "skip" acquisition 
														of crossing semaphores if the car comes right after another going in the exact direction*/
		int tNum = (reqSems[car.dir][5] < 0) ? 2 : 6;	//Is it a right turn or a left/straight "turn"?
		for (int i = 0; i < tNum; i++)
			sem_wait(&semaphores[reqSems[car.dir][i]]);	//Even if you have the headOfLine lock, you'll need all 2/6 semaphores to leave the head of the line
	}
}

//"cross" function
void CrossIntersection(struct Car car) {
	sem_post(&headOfLine);							//Only after obtaining all 2/6 necessary semaphores can a car give up the of headOfLine lock
	reqSems[car.dir][6]++;							//Increment index 6 in on the pathway this car is currently taking, effectively keeping track of the number of cars currently crossing at each pathway
	DisplayCar(car); printf("crossing\n");
	int startTime = currentTime;
	int crossTime = (car.dir % 3 + 1) * 10;			//Right = 10, Straight = 20, Left = 30 
													//*This is why the reqSum six item arrays are organized this way
	sem_post(&semaphores[reqSems[car.dir][0]]);

	//left turn arrival at 500: 
	// 530 (when it will be done) <= 500 (current time that will continue counting up) 
	while (1) {
		if (startTime + crossTime <= currentTime) break;
		usleep(10);
	}
}

//"exit" function
void ExitIntersection(struct Car car) {
	DisplayCar(car); printf("exiting\n");
	carCount--;
	int tNum = (reqSems[car.dir][5] < 0) ? 2 : 6;	//Is it a right turn or a left/straight "turn"?
	
	//Only free the crossing semaphores if there are no more cars coming down this pathway
	if (reqSems[car.dir][6] > 0) reqSems[car.dir][6]--;
	if (reqSems[car.dir][6] == 0)					
		for (int i = 1; i < tNum; i++)
			sem_post(&semaphores[reqSems[car.dir][i]]);
}

//Each car thread calls "arrive", "cross", and "exit" functions from here
void Car(struct Car car) {
	ArriveIntersection(car);
	CrossIntersection(car);
	ExitIntersection(car);
}

//Middle man function used because you can't pass parameters to a thread
void carThread() {
	struct Car car = cars[bufferCar];
	Car(car);
}

int main(void) {
	//initalize 24 street crossing semaphores, setting their values to 1 for binary mutual exclusion
	for (int i = 0; i < 24; i++) sem_init(&semaphores[i], 0, 1);	//(Only one car can be present at a time, no crashing!)
	sem_init(&headOfLine, 0, 1);

	pthread_t* threads;		//Declare a thread pointer and allocate 8 spaces for it to run 8 cars simultaneously
	threads = (pthread_t*)malloc(sizeof(pthread_t) * 8);	

	/*
	Keep the program running while...
	>Only cars 0 - 7 are crossing (currentCar)
	>there is at least one car still present in the intersection (carCount)
	*/
	while (currentCar < 8 || carCount > 0) {

		//Create the thread for the car once its arrive time has been reached by the global counter
		if (currentCar < 8 && currentTime >= cars[currentCar].arriveTime) {
			
			pthread_create(&threads[currentCar], NULL, (void*)carThread, NULL);
			
			bufferCar = currentCar;
			currentCar++;
		}
		usleep(1000);
		currentTime++;
	}
	getchar();

	return 0;
}