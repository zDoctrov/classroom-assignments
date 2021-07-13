/*
Sorting Playing Cards
*Author: Zachary Doctrove
*Date of Assignment: 3/26/2020
*Modification Date: 3/29/2020
This program reads a list of playing cards from the input file,
creates an array of Card type objects, then sorts them with the help
of a custom comparison function.

Changelog:
2019-02-28 Boshen Wang initial version
*/

//#include "pch.h"

#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <cctype>
#include <algorithm>
#include <vector>
#include <list>

using namespace std;

// Utility functions
void loadFile(string fname, fstream& file)
{
	file.open(fname.c_str());
	if (file.fail())
	{
		cout << "Cannot open file " << fname << endl;
	}
}

// converts string to lowercase
string lowercase(string s)
{
	for (unsigned int i = 0; i < s.length(); i++)
	{
		s[i] = std::tolower(s[i]);
	}
	return s;
}


// Class representing a playing card ***NOTE: These are in a LIST structure, not a SPLAY structure
class Card
{
public:
	string value; // 2, 3, 4, ..., Q, K, A
	char suit; // C, D, H, S
	// constructor
	// no input validation here!
	Card(string v, char s) : value(v), suit(s) {}

	bool operator < (Card& c) const;		// Compares the current "node's" playing card to another playing card, which is denoted as input c
	void print() const;
};

int valueTranslator(string value)
{
	int number;

	if (value == "J")
	{
		number = 11;
	}
	else if (value == "Q")
	{
		number = 12;
	}
	else if (value == "K")
	{
		number = 13;
	}
	else if (value == "A")
	{
		number = 14;
	}
	else
	{
		number = stoi(value);
	}

	return number;
}

// Less than comparison operator overload
// INPUT: a Card c
// OUTPUT: True if 'this' Card is less than Card c, False otherwise
bool
Card::operator < (Card& c) const
{
	// Need to compare numbers with letters (value), and just letters if the numbers are equal (suit)

	int cValue = valueTranslator(c.value);
	int thisValue = valueTranslator(this->value);

	// Your code here
	
	if (cValue > thisValue)
	{
		return false;
	}
	else if (cValue == thisValue)
	{
		if (c.suit > this->suit)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	else
	{
		return true;
	}
	
}

// prints out a string representation of the Card
void
Card::print() const
{
	cout << this->value << " " << this->suit << endl;
}


struct twoLists
{
	list<Card> leftList;
	list<Card> rightList;
};


twoLists mergePartition(list<Card> cards, int halfTheSize)
{
	twoLists splitList;

	if (cards.size() > 1)
	{
		for (int i = 1; i <= halfTheSize; i++)	// leftList takes the first half of the list, taking the greater portion if odd.
		{
			splitList.leftList.push_back(cards.front());
			cards.pop_front();
		}
		while (!cards.empty())					// rightlist takes the rest of the nodes in 'cards'
		{
			splitList.rightList.push_back(cards.front());
			cards.pop_front();
		}
	}

	return splitList;
}


list<Card> merge(list<Card> list1, list<Card> list2)
{
	list<Card> mergedList;

	while (!list1.empty() && !list2.empty())
	{
		if (list1.front() < list2.front())
		{
			mergedList.push_back(list1.front());
			list1.pop_front();
		}
		else
		{
			mergedList.push_back(list2.front());
			list2.pop_front();
		}
	}

	while (!list1.empty())
	{
		mergedList.push_back(list1.front());
		list1.pop_front();
	}

	while (!list2.empty())
	{
		mergedList.push_back(list2.front());
		list2.pop_front();
	}


	return mergedList;
}



// INPUT: a list of Cards
// OUTPUT: a sorted list of Cards (descending order)
list<Card> mergeSort(list<Card> cards)
{
	// Your code here
	twoLists partitionedList;

	if (cards.size() > 1)
	{
		partitionedList = mergePartition(cards, (cards.size() + 1) / 2);

		partitionedList.leftList = mergeSort(partitionedList.leftList);
		partitionedList.rightList = mergeSort(partitionedList.rightList);

		cards = merge(partitionedList.leftList, partitionedList.rightList);
	}

	

	return cards;
}

int main()
{
	string inputFilename = "input.txt";
	string line;

	list<Card> cards = list<Card>();
	// open input file
	fstream inputFile;
	loadFile(inputFilename, inputFile);
	while (getline(inputFile, line))
	{
		// trim whitespace
		//line.erase(line.find_last_not_of(" \n\r\t") + 1);
		// echo input
		cout << line << endl;
		// parse input using a stringstream
		stringstream lineSS(line);
		string token;
		string command;
		// store tokens in a vector
		vector<string> tokens;
		while (getline(lineSS, token, ' '))
		{
			// trim whitespace
			token.erase(token.find_last_not_of(" \n\r\t") + 1);
			tokens.push_back(token);
		}

		if (tokens.size() > 0)
		{
			command = tokens[0]; // first token is the command
		}
		else
		{
			command = "";
		}

		if (command == "card")
		{
			// treat string tokens as a single char (first character)
			cards.push_back(Card(tokens[1], tokens[2][0]));
		}
		if (command == "sort")
		{
			cards = mergeSort(cards);
		}
		if (command == "print")
		{
			for (Card c : cards)
			{
				c.print();
			}
		}


	}
	inputFile.close();
	return EXIT_SUCCESS;
}