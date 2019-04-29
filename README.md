# Milestoner

Milestoner website is a servlet driven java application which allows users to register and login and then set milestones, functionality is similar to Trello (a popular project management tool)

## Requirements

To run it you need [Java 8], [Maven] and [Intellij]

Additionally you need a MySQL Database structure installed, we used WAMP to host the database since we were using it previously from other websites.

# Get started

In the SQL folder is a SQL file which automatically creates the database and tables - on phpmyadmin click import and select the file to import it.

You will need to configure the DB class under the util module, Database connection information is setup via constants which should be obvious and self explanatory.

In intellij -> file -> project structure -> Project
	You will need to ensure an SDK is selected, we used 1.8 during development.

In intellij -> file -> project structure -> Modules
	On the right panel expand the collapsing folder src and highlight main, then click 'Sources' with the blue folder icon

In intellij -> file -> project structure -> libraries
	These will likely need to be repointed to local sources (sorry).
	You can use the + sign to add 'From Maven' and then search for the source names

Once the libraries are fixed, the project main class can be set if it isn't already.
	run (at top) -> edit configurations -> select the '...' next to main and navigate to main which is src->main->milestoner->Main
	You may also need to set the 'use classpath of module' to milestoner.
    
You can now run the server and access it from the web browser using: 
	localhost:9001

You can use the default login with example project already setup or create a new user, all functionality should be immediately obvious upon use.
	email: 		test1@gmail.com
	password: 	abcd1234