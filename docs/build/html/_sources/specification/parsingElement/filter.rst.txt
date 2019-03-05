
_`filter`
------------------------

The filter field determines whether the value of a `Parsing Element`_ 
(complex or simple tagType does not matter) is added to the object tree. 
The filter field is an expression_ that returns true or false. 


The following objects are available in Expression Context.

.. seealso::

   expression_

.. csv-table::
    :header: Name, Data Type, Description, Example
    :stub-columns: 1
    :delim: |
    
    params_ | Map<string,any> | params_ object. | **params.dateFormat** =='dd.MM.yyyy' 
    self_ | Node_ | current node object that hold data of current complex tagType_ | **self.data.foo** => foo field of current node,  **self.parent.data.foo** => foo field of parent node, **self.data.bar.foo** => foo field of bar object in current node.
    all_ | Map<string,Node_> | A map that stores all nodes by the "uniqueName_" of `Parsing Element`_  | **all.bar.data.foo** => foo field of **bar** node,  **all.barList.data[0].foo** => *foo* field of first item of *barList* node
    value | string | value of the current tag in `source document`_ | **value=='Computer'**,**value.startWith('bar')**


**Examples**:



**Example 1**

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
                 version: 1.0 
                 result
                     tagType: array
                     tagPath:/
                     filter: $self.data.category=='Computer'  # collect all data that category field is 'Computer'  
                     fields:
                      name: string
                      category: string

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"array",
                  "tagPath":"/"  ,
                  "filter": "$self.data.category=='Computer'",         
                  "fields":{
                     "name":"string",
                     "category":"string"            
                   }
               }
             } 

**Example 2**

..  content-tabs::
       
      .. tab-container:: yaml
         :title: YAML

         .. code-block:: yaml
         
           version: 1.0 
           result
               tagType: array
               tagPath:/
               fields:
                name: string
                category: 
                  filter: $value=='Computer' # only assign "category" if "category" is "computer".

      .. tab-container:: json
         :title: JSON

         .. code-block:: json
         
            {
               "version": 1.0,
               "result":{
                  "tagType":"array",
                  "tagPath":"/"  ,              
                  "fields":{
                     "name":"string",
                     "category":{
                        "filter": "$value=='Computer'"
                     }
                   }
               }
            }
    


**Possible Output Of Example 2**

.. code-block:: json

  [{
     "name":"foo",
     "category":"Computer"
   },
  {
     "name":"foo"
  }]
