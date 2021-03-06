![Logo](https://github.com/TechZech/GroceryApp/blob/master/UPC_Data/app/src/main/assets/glistappicon.png?raw=true)

# GroceryList

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)
3. [GIF Walkthroughs](#Gif-Walkthroughs)

## Overview
### Description
*GroceryList* is the new, ideal app for keeping track of your stock of groceries at home, as well as keeping track of groceries when supplies run low. Our app allows you to scan the barcode on any grocery item you buy, and keeps an inventory of it in a database. With this database, users can keep track of their stock, as well as form a grocery list of items to buy next time you go shopping.

### App Evaluation
- **Category:** Productivity
- **Mobile:** User can use their phone's camera to scan grocery barcodes
- **Story:** User can easily keep track of their grocery list and inventory
- **Market:** Targeted towards anyone who buys groceries, generally 18+
- **Habit:** User updates the status of what grocery items they have, generally when consuming or buying more food
- **Scope:** Would need to have access to a database of barcodes (Internet connection)

## Product Spec

### 1. User Stories

**Required Must-have Stories**

* [X] Barcode scanner
* [X] Current inventory
* [X] List of groceries you need to buy
* [X] Login + Registration 

**Optional Nice-to-have Stories**

* [X] Grocery budget tracker
* [X] Nutrition Data
* [X] Recommended Products
* [X] Profile
* [X] Friends and Groups
* [X] Social media aspect

### 2. Screen Archetypes

 - **Scanner:** Utilizes camera to scan any barcode, sends the barcode to a scraper to retrieve item information, and then adds that item to your inventory
 - **Inventory:** RecyclerView of groceries you currently have
 - **Grocery List:** RecyclerView of groceries you want to buy
 - **Profile:** User info and settings
 - **Login Screen:** User can login to their account, or register for a new one
 - **The Feed:** Social Media tab that allows users to see what other grocery items their friends have bought
 - **Explore:** Allows the user to search for other users, groups, posts, and even food items, all from a centralized location

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Barcode Scanner
* Explore Page
* Grocery List and Inventory
* The Feed
* Profile

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Register for a new account
   * Login to profile
   * Register for a new account
* Barcode scanner
   * Tap To Scan brings up the barcode scanner
   * Manually add items that may be unscannable
* Inventory
   * Click on a food item to bring up details about it
      * Nutrition Facts
      * Similar Products
* Explore
   * View details about posts, users, and groups when clicked
* Feed
   * Click on an item in the Feed to view the full post (Location bought, number of likes, comments)
* Profile
   * Edit Profile
   * Shopping History
   * Manage Lists
   * Groups
      * View Specific Groups you are in
   * Friends
      * Friends' Profiles
      * Friend Requests

## Initial Wireframe
![Our Wireframe](https://github.com/TechZech/GroceryApp/blob/master/UPC_Data/app/src/main/assets/wireframe.jpg?raw=true)

## Schema
### Models
#### Users

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | username      | String   | username for login |
   | password      | String   | password for login |
   | email       | String     | email |
   | userID            | String      | Primary key for user |
   | photoUrl       | String     | URL for Profile Picture |
   | visibility       | String     | Public vs Private profiles |
   | Friends       | List< iD >     | List of Friends the user has added |
   | SentFriendRequests       | List< userID >     | List of Friends the user has sent friend requests to |
   
   
   #### Groups

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | gid      | String   | ID Primary Key for a group |
   | visibility       | String     | Public vs Private groups |
   | whoCanPost      | String   | Determines which users can post in the group |
   | groupname       | String     | Name of group |
   | members       | List< userID >     | List of current memebers in the group |
   | owner       | userID     | User who created the group |
   | photoUrl       | String     | URL for Group Picture |
   
   
   #### Posts(Food Items)

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | pid       | String     | ID Primary Key for Post |
   | dateTime       | timestamp     | Date when post was made |
   | imageUrl       | String     | URL for Food Image |
   | inventory       | boolean     | true if in inventory, false if in grocery list |
   | price       | String     | Price of food item |
   | quantity       | int     | How many of a food item |
   | title       | String     | Name of the food item |
   | upc       | String     | UPC code for the food item |
   | user       | user     | User who owns the food item |
   | lat       | double     | latitude |
   | lon       | double     | longitude |
   | numLikes       | int     | Number of likes a post has |
   | placeName       | String     | Name of store food item was bought from |
   | placeid       | String     | ID for placeName |
   | comments       | List< comment >     | Comments on a post |
   
   
### Networking

* Login Screen
	* (Read/GET) Query all user information
	* (Create) Create a new account

* Barcode Scanner Screen
	* (Read/GET) Scrape item information using barcode UPC
	* (Create) Create a new food item object

* Explore Screen
	* (Read/GET) Query all user, groups, and food posts that are tagged as 'visible'

* Inventory Screen
	* (Read/GET) Query inventory list of a user's food items
	* (Update) Update Grocery Items within inventory
	* (Read/GET) Food item information / similar products when clicked
 
* Grocery List Screen
	* (Read/GET) Query grocery list of a user's food items
	* (Update) Update Grocery Items within Grocery List
	* (Read/GET) Food item information / similar products when clicked

* Feed Screen
	* (Read/GET) Query all food posts that are tagged as 'visible'

* Profile Screen 
	* (Read/GET) Query logged in user object
	* (Update/PUT) Update user profile image
	* (Update) Clear a user's list
	* (Read/GET) Query shopping history


## Gif Walkthroughs
	
### Barcode Scanner
<img src='BarcodeScanner.gif' title='Barcode Scanner' width='' alt='Barcode Scanner GIF' />
	
### Inventory and Grocery List
<img src='Inventory.gif' title='Inventory and Grocery List' width='' alt='Inventory and Grocery List GIF' />
	
### The Feed
<img src='Feed.gif' title='Feed' width='' alt='Feed GIF' />
	
### Explore
<img src='Explore.gif' title='Explore' width='' alt='Explore GIF' />
	
### Profile
<img src='Profile.gif' title='Profile' width='' alt='Profile GIF' />
	
### Friends
<img src='Friends.gif' title='Friends' width='' alt='Friends GIF' />
	
### Shopping History
<img src='ShoppingHistory.gif' title='Shopping History' width='' alt='Shopping History GIF' />
	
## Old Walkthroughs

### Gif Walkthrough Week 10
<img src='walkthroughWeek10.gif' title='Video Walkthrough' width='200' alt='Video Walkthrough' />

### Gif Walkthrough Week 11
<img src='walkthroughWeek11.gif' title='Video Walkthrough' width='200' alt='Video Walkthrough' />

### Gif Walkthrough Week 12
<img src='walkthroughWeek12.gif' title='Video Walkthrough' width='200' alt='Video Walkthrough' />

### Gif Walkthrough Week 13
<img src='walkthroughWeek13.gif' title='Video Walkthrough' width='200' alt='Video Walkthrough' />

### Important Links
	
https://firebase.google.com/ - FireBase Database

https://github.com/zxing/zxing - Barcode Scanner
	
https://jsoup.org/ - Java Web Scraping Library
	
https://weeklycoding.com/mpandroidchart-documentation/ - MPandroidChart (Graphs)

https://www.barcodespider.com/ - Barcode Lookup API
