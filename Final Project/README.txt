*******************************
* Project 4/Bioinformatics
* CS 321 Jyh-haw Yeh
* 05/04/2016
* Ian Gilman
* Joshua Holden	
* Prince Kannah	
* Jayden Weaver	
*******************************

OVERVIEW:

This program implements a B-tree data structure and uses it to store 
DNA sequences of a specified length read from a GeneBank file in order to 
determine the frequency of particular subsequences. The resulting B-tree 
is created and written to a binary file with the GeneBankCreateBTree program. 
The generated binary file can be searched using the GeneBankSearch program 
to compare sequences of length k from a query file to sequences stored 
in the B-tree file.

INCLUDED FILES:

 * BTree.java - source file - Implements a B-tree data structure

 * BTreeNode.java - source file - Used to model nodes of a B-tree

 * ConvertDnaToLong.java - source file - Class to generate a key from the DNA 
sequence strings

 * GeneBankCreateBTree.java - source file - Driver class that creates B-tree 
file

 * GeneBankSearch.java - source file - Driver class that searches B-tree using 
query file

 * OptimalDegreeGenerator.java - source file - Generates optimal degree for 
B-tree

 * Parser.java - source file - Parses GeneBank file for DNA sequences

 * TreeObject.java - source file - Models TreeObjects contained in each node

 * TreeWriter.java - source file - Handles binary file IO

 * README - this file


COMPILING AND RUNNING:
 
 From the directory containing all source files, compile program 
(and all dependencies) with the command:

 $ javac *.java

 OR

 From the directory containing the makefile, compile the program with 
the command:
 
 $ make build

 Run the compiled programs with the commands:

 $ java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]

 $ java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]


PROGRAM DESIGN:

The core of the program is the BTree class, which implements a B-tree data 
structure made up of BTreeNodes. Each BTreeNode contains some metadata (including 
the maximum number of keys allowable and 2 boolean variables to flag whether 
the node is the root or a leaf), an array list of TreeObjects, and an array list 
of child pointers which are integer offsets. The BTree class includes methods to
insert objects into the B-tree, to split a node when needed, and to write the 
B-tree to a file. This class has two constructors, one to create a new B-tree 
and prepare to write it to a file, and another that is able to access an 
existing B-tree file to use its contents.

The TreeObject class models the individual objects stored in each node of 
the B-tree. Each TreeObject contains a key and a frequency. The key is derived 
from the DNA sequence that is read in by the Parser class and converted to a 
long data type binary representation. 

Each time the B-tree is written to a file, the metadata (consisting of 20 bytes)
 is written first, followed by all of the nodes of the B-tree. Each node 
contains 16 bytes of metadata, an array list of TreeObjects (12 bytes each, 
8 bytes for the key + 4 bytes for the frequency) and an array list of 
integers (4 bytes) to represent the offset of the children in the form 
of indexes relating to each node. 


TESTING:

The following tests were performed using the GeneBankCreateBTree class (debug level 0):

Degree		Sequence Length		Cache Size	File		Time (ms)
-----------------------------------------------------------------------------------------------------
2		10			NONE		test3.gbk	534
2		10			100		test3.gbk	483
2		10			500		test3.gbk	458

2		10			NONE		test5.gbk	374883
2		10			100		test5.gbk	381960
2		10			500		test5.gbk	395885

128		10			NONE		test3.gbk	529
128		10			100		test3.gbk	348
128		10			500		test3.gbk	364

128		10			NONE		test5.gbk	365117
128		10			100		test5.gbk	387573
128		10			500		test5.gbk	362988


The following tests were performed using the GeneBankSearch class (debug level 0):

Degree		Sequence Length		Cache Size	Files (btree, query)			Time (ms)
-----------------------------------------------------------------------------------------------------
2		7			NONE		test3.gbk.btree.data.7.2, 	query7	805
2		7			100		test3.gbk.btree.data.7.2, 	query7	387
2		7			500		test3.gbk.btree.data.7.2, 	query7	365

2		7			NONE		test5.gbk.btree.data.7.2, 	query7	817
2		7			100		test5.gbk.btree.data.7.2, 	query7	422
2		7			500		test5.gbk.btree.data.7.2, 	query7	463

128		7			NONE		test3.gbk.btree.data.7.128, 	query7	604
128		7			100		test3.gbk.btree.data.7.128, 	query7	355
128		7			500		test3.gbk.btree.data.7.128, 	query7	330

128		7			NONE		test5.gbk.btree.data.7.128, 	query7	616
128		7			100		test5.gbk.btree.data.7.128, 	query7	333
128		7			500		test5.gbk.btree.data.7.128, 	query7	319

--------------------------------------------------------------------------------------
