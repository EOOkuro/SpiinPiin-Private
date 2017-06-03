# User management apis
* The base path would be at http://localhost:8080/users/api if the default configurations have not been changed. **Remember to change localhost, port and api path as a propriate if you have made any changes to teh configuration files**

## Objects
### User object 
#### Mandatory fileds
	* _id **Federated ID from OAuth provider**
	* fedemail **Email registered with the OAuth provider**
	* provider **The provider i.e. GOOGLE, FACEBOOK**
	* name **The name of the user registering**
	* gender **The user gender**
	* phone **User phone number**
	
#### Other fields
	* email **User prefered email address**
	* primaryfedid **not used for now** 
	* id **User id, i.e. national id number/passport number/driving license number**

* Other fields can be added on-the-fly based on frontend needs. 

### TResult object
	* status **0 (failed), 1 (success), or -1 (special fail i.e. missing values/user)**
	* data **Payload**
	* msg **Message passed across for the success or failure**

### Session object
	* key
	* name

## Apis
### Register user
* */register* POST operation with User properties as Json object. **Returns TResult with data of session object, status code of 0 for missing required fileds, 1 for success**

### Update user
* */update* PUT operation with User properties as Json object **Returns TResult with status code of 0 for fail, and 1 for success**

### Signin
* */signin* GET operation with **header** set for 'key', with the key value returned after initial sign in or 'fedid' and 'idprovider' where user is loging in a fresh. **idprovider is the OAuth provider i.e. GOOGLE**. **Returns TResult with data of session object, status code of -1 for missing user, 0 for failed signin and 1 for success**

### Get user details
* */user* GET **Returns TResult with data of User object, status code of 0 for failed and 1 for success**

### Ping
* */* GET **Returns text 'ok' as an indicator that the server is up**