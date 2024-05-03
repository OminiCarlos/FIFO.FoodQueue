# FIFO_Snack_Stack
A meal planning assistant that reduces food waste by reminding user of about-to-expire ingredients.
You do not need to worry about having to throw that ugly goo at the corner of your fridge anymore!

## What will the application do?
- Lets users create food items
- Remind users of about-to-expire food, so they can cook them first.
- Keep track of the food inventory.

## Who will use it?
Every family that cooks at home.
- Busy students who don't want to spend time to check their food stock often.
- Restaurant owners who need to keep track of food stock and decide the ingredients to be sold quickly.

## Why is this project of interest to you?
Simply look at the corner of your fridge: is there a moldy orange or a mummy of a garlic hiding there?
Let's be honest, we all have wasted some food because we forgot to use it. According to FAO (Food and Agriculture
Organisation), an estimated 17 percent of total global food production is wasted in households, in the food service
and in retail all together. The best way to limit food waste is to prevent it. Instead of developing safe food
preservatives, or new packaging technologies, planning the meal is proven to be most effective against food waste.

Unfortunately, having a thorough meal plan is a major challenge.
There are a lot of things to consider: The cooking complexity; The quantity and expiration date of ingredients;
Nutrition values... to name a few. FIFO_Snack_Stack adopts the FIFO (first-in-first-out) principle to help users
effectively manage the food inventory .

Learning Java lets me understand the power of programming: we can make a large impact with just a simple app.
Based on my experience and knowledge, I want to help reduce food waste.

## User story

- As a user, I want to be able to add a food item to my fridge.
- As a user, I want to be able to view a list of item and quantity in the fridge.
- As a user, I want to be able to put food items in a freezer, where the shelf life is extended;
- As a user, I want to be able to save my inventory list to file (if I so choose)
- As a user, I want to be able to be able to load my inventory list from file (if I so choose);

[//]: # (- As a user, I want to be able to set items to open-package, which means the expiration date is set to 3 days from today;)


# Instructions for Grader

- You can generate the first required action related to the user story "add a food item" by filling the 
    fields in the bottom-right panel, and clicking "add" button.
- You can generate the second required action related to the user story "put food items in a freezer" by select a row in
    the inventory, add click the transfer button in the bottom-left panel. 
- You can locate my visual component by checking the icon of the app.
- You can save the state of my application by clicking the "save" button.
- You can reload the state of my application by clicking the "load" button.

# Phase 4: Task 3

- If there would be more time for me, I would move the TrackerFrame class out of the GUI class, for more clarity.
- I would apply a bi-directional relationship between the food and all storage spaces. This way a food is always 
associated with a storage. 
- I would implement the composite pattern in the inventory class, as it has a tree structure. 