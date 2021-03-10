Original App Design Project - README Template
===

# Grocery List Tracker

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Allows you to scan bar code of stuff you buy and keeps an inventory of it in a local sqllite db and user can click on the item to 'remove' it whenever it is consumed and when there are none left, alert the user. This can be used to build up a new grocery list automatically so the user has an all in 1 app for grocieries

### App Evaluation
- **Category:** Productivity
- **Mobile:** User can user their phone's camera to scan barcodes.
- **Story:** User can easily keep track of their grocery list
- **Market:** Anyone who buys groceries, generally 18+
- **Habit:** User updates the status of what's being consumed whenever they consume groceries (at least 3 times a day)
- **Scope:** Would need to have access to the inventories of nearby grocery stores? App is pretty clearly defined

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Barcode scanner
* Current inventory
* List of groceries you need to buy

**Optional Nice-to-have Stories**

* Grocery budget
* Healthy foods tracker
* Profile
* Weigh food - package already has weight printed on it
* Social media aspect?
    * 

### 2. Screen Archetypes

* Scanner
   * Get access to camera to scan the barcode of groceries
* Grocery list
   * RecyclerView of groceries you need to buy
* Inventory
   * Checklist of groceries you currently have
* Profile
   * User info, settings
* Login Screen
    * User can login to their account
* Registration Screen
    * Create a new account

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Scanner
* Grocery List
* Inventory
* Profile

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Login to profile
* Registration Screen
   * Create a new account
* Inventory
   * Once you're logged in, you can switch tabs
* Food description
    * Click on a food item to learn more. Ex: bought date, initial size, how often bought

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

## Schema
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

### Important Links

https://github.com/zxing/zxing

https://developers.google.com/ml-kit/vision/barcode-scanning/android

https://barcodeapi.org/index.html#auto
