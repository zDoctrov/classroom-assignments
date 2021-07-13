/*
 *Author: Zachary Doctrove
 *Creation Date: 2/28/2019
 *Purpose: Code for Simple Ordered Map ADT Implementation based on AVL Trees
 *NOTE: only basic methods of the Order Map ADT (i.e., put, erase, find, size, empty) implemented

 2019-10-10 BW changed values from int to string, adapted to AVL Tree,fixed several bugs
 2020-01-15 BW reworked input, simplified to BST sum, add pretty print
*/

//#include "pch.h"

#include <iostream>
#include <cstdlib>
#include <cstring>
#include <string>
#include <fstream>
#include <cstddef>
#include <cctype>
#include <sstream>
#include <algorithm>
#include <vector>

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

/*
 *Purpose: Class definition of a partial implementation of an ordered map ADT,
 mapping integers keys to nothing, using a binary search tree (BST)
 *NOTE: For simplicity, and consistency with implementation in other programming languages (e.g., Java and Python), the documentation below refers to variables that are actually pointers to a node in the tree simply as a node in the tree, without the pointer qualification, whenever the distinction is clear from the context
 *NOTE: For consistency with implementation in other programming languages, this implementation does not overload the ++ operator to implement the successor function; similarly for the -- and the predecessor function
*/

class BSTMap
{
	// simple data structure used to create nodes for (linked-list) BST-based implementation of an ordered map ADT
public:
	struct Node {
		int key;
		int ht;
		int sum;
		Node* left;
		Node* right;
		Node* parent;
		Node(int k, int s) :
			key(k), sum(s), left(NULL), right(NULL), parent(NULL) { }
		Node(int k, int s, Node* l, Node* r, Node* p) :
			key(k), sum(s), left(l), right(r), parent(p) { }

	};
public:
	typedef BSTMap::Node Node;
public:
	Node* root;
private:
	int n;
protected:
	void printAux(const Node* w) const;  // print utility
public:
	BSTMap() : root(NULL), n(0) { };
	Node** find(int k) const;
	Node* put(int k);
	void calcSum(Node* s);
	int size() const;
	bool empty() const;
	void print() const;   // print as parenthetic string
	void printTree(Node* s, int space) const;
};

/*
 *Purpose: Implement member functions/methods of BSTMap class
*/

// utility/aux function to print out a parenthetic string representation of the BST
// INPUT: a node w in the BST whose subtree is to be printed out; or NULL
void
BSTMap::printAux(const Node* w) const {
	if (w) {
		cout << "[" << w->key << ":" << w->sum << "]";
		cout << "(";
		printAux(w->left);
		cout << "),(";
		printAux(w->right);
		cout << ")";
	}
}

// print out a parenthetic string representation of the whole BST
void
BSTMap::print() const {
	printAux(root);
	cout << endl;
}

// prints out a string representation of the whole BST using a reverse inorder traversal
void
BSTMap::printTree(Node* s, int space) const {
	int addSpace = 8;
	// base case
	if (!s)
	{
		return;
	}
	// add more whitespace
	space = space + addSpace;
	// print right
	this->printTree(s->right, space);

	cout << endl;
	for (int i = addSpace; i < space; i++)
		cout << " ";
	cout << s->key << ":" << s->sum << endl;

	// print left
	this->printTree(s->left, space);
}


// INPUT: a key k, as an integer
// OUTPUT: a 2-element array, where
// element 0 is w, the node with key k, if k is already in the ordered map, or NULL otherwise; and
// element 1 is z, the parent of node w, or NULL if w is NULL or the root
// or the last node visited while trying to find a node with key k in the BST
BSTMap::Node**
BSTMap::find(int k) const {
	Node* w = root;
	Node* z = NULL;
	while (w && (w->key != k)) {
		z = w;
		w = (w->key > k) ? w->left : w->right;
	}
	Node** wAndZ = new Node*[2];
	wAndZ[0] = w;
	wAndZ[1] = z;
	return wAndZ;
}

// INPUT: an integer key
// OUTPUT: if k is already in the ordered map, then output the node containing k
// otherwise, output the new node in the tree with the corresponding key k.
// POSTCONDITION: a node with key k exists in the BST
// if the BST was empty, then the new node becomes the root of the BST (and thus its only node)
BSTMap::Node*
BSTMap::put(int k) {
	Node** wAndZ = find(k);
	Node* w = wAndZ[0];
	Node* z = wAndZ[1];
	delete wAndZ;
	if (w) {
		return w;
	}
	Node* x = new Node(k, 0, NULL, NULL, z);
	if (z) {
		if (z->key > k) z->left = x;
		else z->right = x;
	}
	else root = x;
	n++;
	if (n == 1) root = x;
	return x;
}

// INPUT: a pointer to a Node in the tree s 
// PRECONDITION (Values that will work for the function): The binary tree has at least one node within it 
// in order for the function to even consider summing key values

// POSTCONDITION: From the selected node to all branching children (the subtree), a sum will exist by each node's key value. 
// This sum value is based on the key values of a node and all its children's key valuees. 
void
BSTMap::calcSum(Node* s) {							// Goes through each node via a Preorder Traversal

	if (s)											// Checks to see if the current node is already NULL, just in case
	{

		if (s->left == NULL && s->right == NULL)	// Base Case
		{
			s->sum = s->key;						// If there are no descendants, the "subtree" sum is equal to the keyvalue alone
		}
		else										// Recursive Case
		{
			s->sum = s->key;

			if (s->left != NULL)
			{
				calcSum(s->left);
				s->sum = s->sum + s->left->sum;		// Add the key and left node sum to the current node's sum
			}

			if (s->right != NULL)
			{
				calcSum(s->right);
				s->sum = s->sum + s->right->sum;	// Add the the right node sum to the current node's sum
			}
		}
	}
}


// OUTPUT: size of the tree
int
BSTMap::size() const {
	return n;
}

// OUTPUT: true if the tree is empty; false otherwise
bool
BSTMap::empty() const {
	return (!root);
}

int main() {
	string inputFilename = "input.txt";
	string line;

	BSTMap B;
	// open input file
	fstream inputFile;
	loadFile(inputFilename, inputFile);
	while (getline(inputFile, line))
	{
		// trim whitespace
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

		if (command == "put")
		{
			// insert a node for each key specified
			for (unsigned int i = 1; i < tokens.size(); i++)
			{
				B.put(stoi(tokens[i]));
			}
		}
		if (command == "sum")
		{
			B.calcSum(B.root);
		}
		if (command == "print")
		{
			B.print();
		}
		if (command == "printTree")
		{
			B.printTree(B.root, 0);
		}


	}
	inputFile.close();
	return EXIT_SUCCESS;
}

