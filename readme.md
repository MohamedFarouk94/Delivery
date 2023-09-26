// This is an ongoing project. Expect this file to be frequently edited until the end of the project.

# Delivery

## Introduction
*Delivery* is an E-Commerce system, or it can be seen as a *Talabat* clone. It can provide an opportunity for sellers, delivery persons (They are called 'pilots' in the terminology of this project) and customers to play their roles in an E-Commerce process.
Delivery consists of a Backend API, an Adnroid application for customers and pilots, and a desktop application for sellers and staff members.

## Features
### 1. Backend API
* All users (sellers, customers, pilots or staff members) can use API to implement their meant actions as seen below in API endpoints.

### 2. Android app
* Customers and pilots can use Delivery Android app as a third party app to implement the customer/pilot related actions.
* *Delivery* Android app provides a shortlist feature for customers to store their marked items on their local machines.
* *More details to be annouced later*

### 3. Desktop app
* Sellers and staff members can use Delivery desktop app as a third party app to implement the seller/staff related actions.
* *More details to be annouced later*

## API Endpoints
| Method | Endpoint | Action | Requires Authorization? | User Type | Data required |
| --- | --- | --- | --- | --- | --- |
| GET | /login | Login to the system | No | Any | username<br>password | 
| GET | /items | Getting all stored items | No | Any | |
| GET | /items/:ItemId | Getting a specific item | No | Any | |
| GET | /items/:ItemId/reviews | Getting reviews for a specific item | No | Any | |
| GET | /sellers | Getting all stored sellers | No | Any | |
| GET | /sellers/:SellerId | Getting a specific seller | No | Any | |
| GET | /sellers/:SellerId/items | Getting items of a specific seller | No | Any | |
| POST | /items/add | Adding a new item | Yes | Seller | name<br>price<br>description |
| PUT | /items/:ItemdId/edit | Editing an item data | Yes | Seller | name<br>price<br>description |
| DELETE | /items/:ItemId/delete | Deleting an item | Yes | Seller | |
| PATCH | /items/:ItemId/set-image | Attatching an image to an item | Yes | Seller | b64img |
| GET | /basket | Getting customer's basket | Yes | Customer | |
| POST | /items/:ItemId/add-to-basket | Adding an item to customer's basket | Yes | Customer | quantity |
| DELETE | /items/:ItemId/remove-from-basket | Removing an item from customer's basket | Yes | Customer | |
| POST | /make-order | Making an order with all items in customer's basket | Yes | Customer | region |
| DELETE | /orders/:OrderId/cancel | Canceling an order | Yes | Customer | |
| POST | /items/:ItemId/send-review | Posting a review for an item | Yes | Customer | rating<br>text |
| GET | /orders | Getting orders related to user | Yes | Customer/Pilot | |
| GET | /orders/:OrderId | Getting a specific order | Yes | Customer/Pilot | |
| POST | /orders/:OrderId/review | Posting a review for an order | Yes | Customer/Pilot | rating<br>text |
| GET | /available-orders | Getting orders in need to a pilot in a pilot's region | Yes | Pilot | |
| PATCH | /orders/:OrderId/accept | Accepting an order | Yes | Pilot | |
| PATCH | /orders/:OrderId/drop  | Dropping an order | Yes | Pilot | |
| PATCH | /orders/:OrderId/complete | Confirming an order is successfully done | Yes | Pilot | | 
| PATCH | /orders/:OrderId/report | Reporting a problem with customer | Yes | Pilot | |

* More details about API usage are to be announced later.
## Technologies
* *Django REST Framework* as an API framework.
* *SQLite* as a database engine.
* *Kotlin Jetpack Compose* in Android app developing.
* *Technology for desktop app is to be declared later*
* *Highlighted used libraries in backend and applications are to be declared later*

## Authors
[Mohamed Farouk](https://github.com/MohamedFarouk94/)

## License
*TBA*