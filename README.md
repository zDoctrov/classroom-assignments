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
  -First it names the container process as xv6cp<br />
  -Then it mounts a local directory called "xv6" under your PC's user name. This makes the directory accessible on both your host machine and the virtual machine<br />
  -Finally, it downloads the shqwang/xv6 container image. Replace "yourname" with the local username you use to login into your operating system<br /><br />
  (Linux & Apple users)<br />
  **$ docker run --name xv6cp -v /Users/yourName/xv6:/xv6 -it shqwang/xv6 bash**
  <br /><br />
  (Windows users)<br />
  **$ docker run --name xv6cp -v /c/Users/yourName/xv6:/xv6 -it shqwang/xv6 bash**

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

