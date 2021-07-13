# Table of Contents

**[1. Overview of *classroom-assignments*](#heading--1)**

**[2. Operating Systems (Winter 2021)](#heading--2)**

**[3. Computer Networks & Distributed Processing (Winter 2021)](#heading--3)**

**[4. Java Programming (Fall 2020)](#heading--4)**

**[5. Software Engineering I (Fall 2020)](#heading--5)**

**[6. Data Structures & Algorithm Analysis (Winter 2020)](#heading--6)**

<!-- Formatting of Table of Contents
    # Table of Contents

**[1. Overview of *classroom-assignments*](#heading--1)**

  * [1.1. Purpose of this repository](#heading--1-1)
  * [1.2. ...](#heading--1-2)

**[2. ...](#heading--2)**

  * [2.1. ...](#heading--2-1)

      * [2.1.1. ...](#heading--2-1-1)

  * [2.2. ...](#heading--2-2)
  * [2.3. ...](#heading--2-3)
 -->

----

## 1. Overview of *classroom-assignments* <a name="heading--1"/>
A single repository to rule them all. Placed here are all of the programming assignments I've done previously during my undergraduate years at UoM Dearborn.
I've organized them in terms of the respective class they were a part of and ordered these classes in terms of their recency. 

----

## 2. Operating Systems (Winter 2021) <a name="heading--2"/>
For this course I was tasked with running xv6, an educational operating system, in a container using Docker to virtualize the process.
All of the code written and modified was made in C in visual studio and ran/tested in a directory that both my host machine and xv6cp container could access.


### Getting Docker Running

Follow the instructions from Docker's main website in order to properly install their software on your preferred operating system.  
[Installation page on DockerDocs.](https://docs.docker.com/get-docker/)  
[Similar instructions on DockerHub.](https://hub.docker.com/search?q=&type=edition&offering=community)  

### Generating an xv6 docker process

My professor created an xv6 container image called shqwang/xv6 and stored it on his DockerHub repository.  
You can find it [here](https://hub.docker.com/r/shqwang/xv6/tags?page=1&ordering=last_updated) and the sequence
of terminal commands I've placed below will add a local container process called xv6cp to your local machine.

  1.  The first terminal command does three things at once:<br />
  * First it names the container process as xv6cp<br />
  * Then it mounts a local directory called "xv6" under your PC's user name. This makes the directory accessible on both your host machine and the virtual machine<br />
  * Finally, it downloads the shqwang/xv6 container image. Replace "yourname" with the local username you use to login into your operating system<br /><br />
  (Linux & Apple users)<br />
  **$ docker run --name xv6cp -v /Users/yourName/xv6:/xv6 -it shqwang/xv6 bash**
  <br /><br />
  (Windows users)<br />
  **$ docker run --name xv6cp -v /c/Users/yourName/xv6:/xv6 -it shqwang/xv6 bash**
  <br /><br />

  2. After all the files have fully downloaded, the '$' symbol should now have change into something along the lines of "root@4f8a09f17d24:/# ", which signifies you are in the container process. Since the container process is complete, you can now start building the xv6 operating system within it with the following commmands.
  <br /><br />
  First, copy the original source code copy of xv6 from within "**xv6-public**" into the previously created mounted folder "**xv6**".
  This gives you a way to back up your files in the event that you misplace or delete something important in the **xv6** folder<br />
  __root@4f8a09f17d24:/# cp /xv6-public/* /xv6/__

  <br /><br /> 
  3. The following three commands allow you to access, build, and run the **/xv6** directory<br />
  *  __root@4f8a09f17d24:/# cd /xv6__<br />
  *  __root@4f8a09f17d24:/# make__<br />
  *  __root@4f8a09f17d24:/# make qemu-nox__<br />

  Thus, xv6 is ready and able to take on user made programs.<br />
  To shut down the xv6 container, **press ctrl-a** at the same time and then **press x**.<br /><br />
  To get back into the xv6 container, type the commands:<br />
  __$ docker restart xv6cp__<br />
  __$ docker attach xv6cp__<br />

### Project 1: Running a user program in xv6
1. Insert the file __"spin.c"__ into the xv6 folder on your host computer/container directory
2. Go into the __"Makefile"__ and add the program name under "User Programs" (UPROGS=\), following the formatting of the other programs there (_spin\)

## 3. Computer Networks & Distributed Processing (Winter 2021) <a name="heading--3"/>

## 4. Java Programming (Fall 2020) <a name="heading--4"/>

## 5. Software Engineering I (Fall 2020) <a name="heading--5"/>

## 6. Data Structures & Algorithm Analysis (Winter 2020) <a name="heading--6"/>









<!-- Markdown Notes -->
## Header2

This is a sample readme file for our GitHub Example repo. We're learning Markdown.

Bullet Point List
* This is meant to be an example
* Markdown is really fast
* Pretty cool, right?

Numerical List
1. Item One
2. Item Two
3. Item Three

