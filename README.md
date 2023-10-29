# Assessment Small Booking Project ( Java v20 / SpringBoot v2.7 )

## Solution

### How to run
```shell
mvn clean spring-boot:run
```
The service will be hosted on http://localhost:8080.

H2 database management http://localhost:8080/h2-console

### What I did

- Booking logic
> The booking functionality is implemented in such a way that a guest can select a start and end date for a reservation.\
> When a booking request is made, the system checks for any overlapping dates with existing bookings or blocks.\
> If an overlap is detected, the booking request is rejected, and an appropriate error message is returned. If there is no overlap, the booking is saved in the database. \
> This logic ensures that the same property is not double-booked for the same time period. The system also allows for updating and deleting bookings, with similar checks in place to prevent overlaps during updates.\
> Also I added some apis users to check the availability of a specific property over a certain date range. For example, if a guest is planning their vacation and wants to see if a particular property is available for booking during their vacation dates, they would use this endpoint. The system will then respond with the periods of times as list within the requested date range when the property is available for booking.

- Block logic
> The block functionality is designed for use by the property owner or manager. \
> When block is created it not only returns the created block but also updates any bookings that overlap with the block and return.
> They can block out a range of dates during which the property is unavailable for bookings. \
> When a block is created, the system allows for overlapping blocks. \
> This means if a certain date range is already blocked, the system will allow another block to be created that covers the same dates. \
> This is useful in scenarios where, for example, the property manager needs to repaint some rooms while the owner has already blocked the property for personal use. \
> The system also provides functionality for updating and deleting blocks.

- Use In-Memory Database
> For the database, I utilized H2, an in-memory volatile database. This choice was driven by the recommendation in the brief due to its fast data access capabilities, and it was a good fit for this specific project.

- Validation of Request Body
> In order to ensure the integrity of the data being sent to and received from the API, I have incorporated validation logic for the request body in the application. This includes checking that required fields are provided and that they meet certain criteria (for example, date ranges make sense, guest data is valid). This reduces the chance of erroneous data being saved in the database and aids in maintaining the consistency of the data.

- Exception Handling
> Robust exception handling has been implemented throughout the application. This includes both expected and unexpected exceptions that can occur during the execution of the application. Custom exceptions were used to provide meaningful feedback to the user when an operation fails. For example, when a booking overlap is detected, a custom exception is thrown with a detailed message. This approach ensures that the application can gracefully handle errors and provide useful feedback to the user.

## API Endpoint

### BookingController

- POST /api/bookings: 
> This endpoint is used to create a new booking.\
> The request body should contain a valid Booking object. It returns the created booking upon successful creation.

- GET /api/bookings/{id}: 
> This endpoint is used to fetch a booking by its ID.\
> If a booking with the given ID exists, it returns the booking; otherwise, it returns a 404 Not Found status.

- GET /api/bookings: 
> This endpoint is used to fetch all the bookings available in the system.\
> It returns a list of Booking objects.

- GET /api/bookings/check: 
> This endpoint is used to check available intervals for a property between specific dates.\
> It takes three parameters: propertyId, from, and to. The from and to parameters should be date strings. It returns the available intervals for that property during the specified time period.

- GET /api/bookings/check/{id}: 
> This endpoint is used to check the booking status of a specific booking id.\
> It returns the available intervals for the property associated with the booking.

- PUT /api/bookings/{id}: 
> This endpoint is used to update an existing booking.\
> The request body should contain a valid Booking object with the updated values. If a booking with the given ID does not exist, it returns a 404 Not Found status.

- DELETE /api/bookings/{id}: 
> This endpoint is used to delete a booking by its ID.\
> If a booking with the given ID does not exist, it returns a 404 Not Found status.

### BlockController
- POST /api/blocks: 
> This endpoint is used to create a new block.\
> The request body should contain a valid Block object. \
> Upon successful creation, it not only returns the created block but also updates any bookings that overlap with the block. \
> The response body contains a BlockResponse object, which includes the created block and the updated bookings.

- GET /api/blocks/{id}:
> This endpoint is used to fetch a block by its ID.\
> If a block with the given ID exists, it returns the block; otherwise, it returns a 404 Not Found status.

- GET /api/blocks: 
> This endpoint is used to fetch all the blocks available in the system.\
> It returns a list of Block objects.

- PUT /api/blocks/{id}:
> This endpoint is used to update an existing block.\
> The request body should contain a valid Block object with the updated values.\
> Similar to the create block endpoint, this not only updates the block but also updates any bookings that overlap with the block.\
> If a block with the given ID does not exist, it returns a 404 Not Found status.

- DELETE /api/blocks/{id}:
> This endpoint is used to delete a block by its ID.\
> If a block with the given ID does not exist, it returns a 404 Not Found status.


## Tests

```shell
mvn clean spring-boot:run
mvn test
```

### Unit Test

> Made some unit test code of booking controller.

### Integration Test

- test_create_booking_save_success: 
> This test checks that a booking can be created successfully. \
> It sends a POST request to the /api/bookings endpoint with a booking object. It then asserts that the status code of the response is 200 (OK) and the ID of the created booking is 1.

- test_create_booking_but_not_save_because_overlapped_with_first_book: 
> This test checks whether the application prevents a booking from being created if it overlaps with an existing booking. \
> It sends a POST request to the /api/bookings endpoint with a booking object that overlaps with the booking created in the first test. It then asserts that the status code of the response is 400 (Bad Request) and the error message is "Booking interval was overlapped."

- test_create_booking_save_success_because_not_overlapped_with_first_book:
> This test checks that a booking can be created if it does not overlap with any existing bookings. \
> It sends a POST request with a booking object that does not overlap with any existing bookings and asserts that the status code of the response is 200 (OK) and the ID of the created booking is 2.

- test_create_block_save_success_and_return_disabled_bookings: 
> This test checks that a block can be created and any overlapped bookings are returned in the response. \
> It sends a POST request to the /api/blocks endpoint with a block object that overlaps with the booking created in the third test. It then asserts that the status code of the response is 200 (OK), the block object in the response is not null, and the overlapped booking is returned in the response.

- test_update_booking_but_failed_because_overlapped: 
> This test checks whether the application prevents a booking from being updated if the new date range overlaps with an existing block. \
> It sends a PUT request to the /api/bookings/{id} endpoint with an updated booking object that overlaps with the block created in the fourth test, and then it sends a GET request to the same endpoint to get the booking details. It asserts that the status code of the GET response is 200 (OK) and the guest data of the booking has not been updated.

- test_update_booking_success: 
> This test checks that a booking can be updated successfully. \
> It sends a PUT request with an updated booking object, and then it sends a GET request to get the booking details. It asserts that the status code of the GET response is 200 (OK) and the guest data of the booking has been updated.

- test_available_intervals: 
> This test checks that the application can return the available intervals for a property. \
> It sends a GET request to the /api/bookings/check endpoint with query parameters for the property ID and the date range. It then asserts that the status code of the response is 200 (OK) and the date intervals in the response are as expected.