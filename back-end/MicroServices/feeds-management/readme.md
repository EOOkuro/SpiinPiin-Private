# Feeds management apis
* The base path would be at http://localhost:8081/feeds/api if the default configurations have not been changed. **Remember to change localhost, port and api path as appropriate if you have made any changes to the configuration files**

## Objects
### Comment object 
#### Mandatory fields
	* poster **the user posting**
	* comment **The comment being posted**
	* article **Either a comment being commented on id or the feed id**
	
#### Other fields
	* posted **The date the comment was posted**
	* _id **The comment id**

* Other fields can be added on-the-fly based on frontend needs. 

### Dislike object 
#### Mandatory fields
	* poster **the user posting**
	* flag **True or false. True dislikes, false removes any previous dislike by the user**
	* article **Either a comment being disliked on id or the feed id**
	
#### Other fields
	* posted **The date it was posted**

### Favourite object 
#### Mandatory fields
	* poster **the user posting**
	* flag **True or false. True favours, false removes any previous favour flags by the user**
	* article **Either a comment being disliked on id or the feed id**
	
#### Other fields
	* posted **The date it was posted**

### Likes object 
#### Mandatory fields
	* poster **the user posting**
	* flag **True or false. True likes, false removes any previous like flags by the user**
	* article **Either a comment being disliked on id or the feed id**
	
#### Other fields
	* posted **The date it was posted**

### QueryFilter object 
#### Mandatory fields
	* user **the user querying**
	
#### Other fields
	* _id **The id for a feed or comment. Useful when querying comments on a feed or comments on a comment**
	* filter **The query parameters to be used to filter returned values i.e. {longitude: -38.989876554}. All records are returned if no filter is present**
	* fields ***The preferred fields to be returned. All fields are returned when 'fields' is not specified*

### TResult object
	* status **0 (failed), 1 (success), or -1 (special fail i.e. missing values/user)**
	* data **Payload**
	* msg **Message passed across for the success or failure**

### Feed object
#### Mandatory fields
	* summary **the feed summary**
	* article **The details**
	* poster **The user doing the postings.**
	
#### Other fields
	* _id **The id for a feed or comment.**
	* posted **The date posted**
	* thumbnail **The feed thumbnail**
	* image **The feed image**

## Apis
### Comment user
* */comment* POST operation with Comment properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Add Feed user
* */feed* POST operation with Feed properties as Json object **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Dislike
* */dislike* POST operation with Dislike properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Favourite
* */favourite* POST operation with favourite properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Like
* */like* POST operation with Like properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Comments
* */comments* GET operation with QueryFilter properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Feeds
* */feeds* GET operation with QueryFilter properties as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Remove Comment
* */rcomment*  DELETE operation with Comments properties ('_id and poster) as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**

### Remove Feed
* */rfeed*  DELETE operation with Feed properties ('_id and poster) as Json object. **Returns TResult with data, status code of 0 for failure, or 1 for success**