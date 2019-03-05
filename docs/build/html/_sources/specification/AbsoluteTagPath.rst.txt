
.. _absolute:

*********************
Absolute Tag Path
*********************

--------------------------------------

Absolute Tag Path  is found by **joining all tag name from top to bottom with "/"** character until specified tag.
      
Below example is json representation of array of Pet object.

Absolute tag paths are listed below.
      
.. code-block:: json

   [
     {
    "id": 1,
    "category": {
      "id": 1,
      "name": "Cats"
    },
    "name": "PetNameForm",
    "photoUrls": [
      "url1",
      "url2",
      "url3"
    ],
    "tags": [
      {
        "id": 2,
        "name": "New"
      },
      {
        "id": 2,
        "name": "Cute"
      },      
    ],
    "status": "sold"
   ]

Absolute tag paths of all field in above json document are listed below. 

.. csv-table::
    :header: Field Name, Absolute Path
    :stub-columns: 1
    :delim: |   
    
      | /
      Pet:id | /id
      Pet:category | /category
      Pet:category.id | /category/id
      Pet:category.name | /category/name
      Pet:name | /name
      Pet:photoUrls | /photoUrls
      Pet:photoUrls.(item) | /photoUrls
      Pet:tags | /tags
      Pet:tags.id | /tags/id
      Pet.tags.name | /tags/name
      Pet.status | /status
      

Same Example for XML:

.. code-block:: json

   <Pets>
      <Pet>
         <category>
           <id>1589257917030308320</id>
           <name>Cats</name>
         </category>
         <id>6598053714149410844</id>
         <name>PetNameForm</name>
         <photoUrls>
           <photoUrl>url1</photoUrl>
           <photoUrl>url2</photoUrl>
           <photoUrl>url3</photoUrl>
         </photoUrls>
         <status>sold</status>
         <tags>
           <tag>
             <id>4250197027829930927</id>
             <name>New</name>
           </tag>
           <tag>
             <id>8271965854563266871</id>
             <name>Cute</name>
           </tag>
           <tag>
             <id>3487705188883980239</id>
             <name>Popular</name>
           </tag>
         </tags>
       </Pet>
   </Pets>
      
Absolute tag paths of all field in above XML document are listed below. 
      
.. csv-table::
    :header: Field Name, Absolute Path
    :stub-columns: 1
    :delim: |   
    
         | /
      Pets array |  /Pets  
      Pets array item |  /Pets/Pet  
      Pet:id | //Pets/Pet/id
      Pet:category | /Pets/Pet/category
      Pet:category.id | /Pets/Pet/category/id
      Pet:category.name | /Pets/Pet/category/name
      Pet:name | /name
      Pet:photoUrls | /Pets/Pet/photoUrls
      Pet:photoUrls.(item) | /Pets/Pet/photoUrls
      Pet:tags | /Pets/Pet/tags
      Pet:id | /Pets/Pet/tags/id
      Pet:tags.name | /Pets/Pet/tags/name
      Pet.status | /Pets/Pet/status


.. seealso::

   tagPath_

   tagParentPath_