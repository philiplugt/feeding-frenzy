---
tags: [programming, cs, rit, frenzy]
title: Frenzy Instructions
created: '2023-03-27T17:51:50.000Z'
modified: '2023-03-27T18:18:00.932Z'
---

## Frenzy Instructions

Frenzy was the 2nd project assigned at RIT CS2 course. The project involves the creation of a GUI game within which a player controlling a grid cell can move around and eat other enemy grid cells. 

The project emphasized the learning of Java threads and GUI programming by way of using the MVC software pattern.

## Run

This code has been updated to use Java SE 17

To compile: `javac *.java`

To run: `java FrenzyLauncher [N]`

Optional argument `N` will determine the number of grid cells per row/colummn. Default is 15.

## Description

The design (dependency graph) of Frenzy is found in the `graph` folder. The graph can be loaded on the draw.io website. Or you can view the .png file directly. 

For extra context, the original assignment write up is in the `assignment` folder. There are 2 files, one from Q2 2008 and one from Q2 2007. The 2008 version is the original version that I was assigned in class. However, the 2007 is a more comprehensive version, which also has extra implementation instructions. Apparently, 2007 version was a 2-man project, but due to difficulty level it is likely the professors simplified it a year later.

When I first programmed this I made quite a mess. Dependencies were everywhere and it was very buggy. The issue is that MVC is loosely defined software pattern. This left a lot of room for ambiguity and error for the unwitted learning student. After revisiting this project many years later, applying MVC in a more refined way the code is cleaner and functional. 

By applying the observer pattern the View and Model are properly separated, and the View and Controller can be easily decoupled from the Model and substituted within another View component. Likewise, the Model is fully responsible for the game logic. This does create a couple issues. Because,
 1) MVC was originally ment to be a pattern on a per (GUI) component basis, not application level basis. 
 2) The model is quite large and the grid cell units, which are threads are dependent on the Model for gamestate. In order to decouple the Unit class and Model an interface (named mediator) was applied. Thereby giving the Unit class access to only the fields it requires.
 3) MVC is not ideal for games, generally something like ECS pattern would be more suitable

Anther challenge with threading is preventing race conditions. This is in part achieved by using the `synchronized` keyword in Java. Likewise, when updating the Java Swing GUI, `SwingUtilities.invokeLater()` is used. I am not sure if I cover all cases. Things seems to get funky as higher game speeds. For now it mostly seems to work correctly.
