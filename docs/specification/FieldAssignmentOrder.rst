.. _`assignment order`:

**************************
Property Assignment Order
**************************

-----------------------------------

The property assignment order is very important for the correct operation of the expressions in the filter field and in the default field
Referencing a not existing field in "self.data" can cause NullPointerException.

DSM reads `source document` top to bottom in one pass as a stream. 
Once it reads a tag `source document`, it checks whether absolute_ path of the tag match with tagAbsolutePath_ or taParentAbsolutePath_ of any of `Parsing Element`_ 
if Parsing Element founds, value of tag is assigned according to `Parsing Element`_ definitions.

The Property assignment work as follows:

let's name the tag that is pointed by path_  as **current tag** and the tag that is pointed by parentPath  as **parent tag** 

The property is assigned when **current tag** is closed except attribute_ properties for the XML document. 
The attribute_ properties is assigned at start of **parent tag** by reading attribute value of **parent tag**


Order of the property assignment as follows:
 - the closing of  `current tag`_  is near to the document header(starting of _`parent tag`" for attribute )
 - deeper `current tag`_
 - Parsing Element definition close to the document header.(**assignment start from top to bottom** )

The default_ value of a property is assigned when current tag not exist in source document and  **parent tag"** is closed(for all property, include attribute_). 

default_ value is assigned only once except force_ field is true. if force_ field is true default value is assigned at both start and close of **parent tag**

Order of the default value of property assignment as follows:
 - assure  the property is not assigned  or  force field is  true
 - the closing of `parent tag`_is near to the document header.
 - deeper `parent tag`_
 - Parsing Element definition far to the document header.(**assignment start bottom to top** )
 

Example:

.. code-block:: xml

   <Pets>
      <Pet>
         <category>
           <id>1</id>
           <name>Cats</name>
         </category>
         <id>6598053714149410844</id>
         <name>Van Kedisi</name>
         <photoUrls>
           <photoUrl>url1</photoUrl>
           <photoUrl>url2</photoUrl>
           <photoUrl>url3</photoUrl>
         </photoUrls>
         <status>sold</status>
         <tags>
           <tag>
             <id>1</id>
             <name>New</name>
           </tag>
           <tag>
             <id>2</id>
             <name>Cute</name>
           </tag>
           <tag>
             <id>3</id>
             <name>Popular</name>
           </tag>
         </tags>
       </Pet>
   </Pets>

.. code-block:: yaml

      result: 
        type: array
        path: /
        xml: 
          path: "/Pets/Pet"
        fields:
         id:long
         name: 
         status:
         isPopular:
            default $self.data.tags.stream().anyMatch(s->s.name=='Popular')
         category: 
             type: object
             fields: 
               name:
               id: long
         photoUrls: 
              type: array
              path: photoUrls
              xml: 
                 path: photoUrls/photoUrls
         tags: 
             type: array
             path: tags
             xml: 
               path: tags/tag
             fields: 
                 id:int 
                 name: 



DSM read document  top to bottom.  

- it founds **/Pets/Pet** absolute_ path that match with **result** Parsing Element. Then create a **array** and put first item into the array.

.. code-block:: json
  
  result=[{}]
  
- it founds **/Pets/Pet/category** match with **category** Parsing Element. then it create a **object** and assign it to **category** property
   
.. code-block:: json
  
  result=[{
   "category":{}
   }]
   

- it founds   **/Pets/Pet/category/id** match with **category.id** Parsing Element. then it assign it to **id** property of **category object**. 
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3
   }
   }] 
- it founds   **/Pets/Pet/category/name** match with **category.name** Parsing Element. then the value is assigned
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   }
   }] 
   
 
- it founds **/Pets/Pet/id** match with **id** then the value is assigned
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   }
   "id":1
   }] 


- it founds **/Pets/Pet/name** match with **name** then the value is assigned
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   },
   "id":1,
   "name":"Van Kedisi",
   }] 


- it founds **/Pets/Pet/photoUrls/photoUrl** match with **photoUrls** Parsing Element then the new array is created and assigned
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   },
   "id":1,
   "name":"Van Kedisi",
   "photoUrls":[]
   }] 
   
- it founds **/Pets/Pet/photoUrls/photoUrl** match with **photoUrls** then  the value of **photoUrls** is assigned
   
.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   },
   "id":1,
   "name":"Van Kedisi",
   "photoUrls":["url1","url2","url3"]
   }] 
   
after reading all fields under **/Pets/Pet** path following result generated.


.. code-block:: json
  
  result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   },
   "id":1,
   "name":"Van Kedisi",
   "photoUrls":["url1","url2","url3"],
   "status":"sold",
   "tags":[
         {
            "id":1,
            "name": "New"
         },
         {
            "id":1,
            "name": "Cute"
         },
         {
            "id":1,
            "name": "Popular"
         }
   ]
   
   }] 

- it can't find **/Pets/Pet/isPopular** but **isPopular** property has **default** value. When **/Pets/Pet** (**parent tag**) tag is closed then it's expression is evaluated. The result of expression is assigned to **isPopular** property.

.. code-block:: json

 result=[{
   "category":{
     "id": 3,
     "name": "Cats"
   },
   "id":1,
   "name":"Van Kedisi",
   "photoUrls":["url1","url2",url3"],
   "status":"sold",
   "tags":[
         {
            "id":1,
            "name": "New"
         },
         {
            "id":1,
            "name": "Cute"
         },
         {
            "id":1,
            "name": "Popular"
         }
   ],
   "isPopular": true
   }] 
   
