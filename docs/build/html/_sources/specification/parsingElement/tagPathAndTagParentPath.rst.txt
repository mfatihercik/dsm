   
_`path` and _`parentPath`
-------------------------------

The path and parentPath fields indicate which tags are used in the `source document`_ 
during parsing. The value of those fields are regular expressions.

The **path** field specifies the location of a tag in the `source document`_
relative to the path field of the higher-level `Parsing Element`_ definition. 
The default value is the value in the fieldName_ field.

The **parentPath** field is used in a slightly more complex parsing definitions. 
it holds path to parent tag of the tag specified in the "path" field.


The path_ and parentPath_ fields can be defined as the relative path as in unix. 
Relative paths are resolved according to the structure in the DSM document, 
not by the structure in the source file.

Some example of relative path:

:current: ./

:parent: ../

:parentOfParent: ../../

:categoryInParent: ../category

:categoryInCurrent: ./category

To find the exact tag path for the current `Parsing Element`_, starting from the result field, 
all the *parentPath* and *path* fields from top to bottom are merged with the "/" character. 

**tagParentAbsolutePath** and **tagAbsolutePath**  evaluated as follow:

         **currentObject** mean is `Parsing Element`_ that current path and parentPath is defined
         **parentObject** mean is parent `Parsing Element`_ of currentObject      
         
         
         
         - parentObject = if (**parentPath+path**) is relative path then find  parentObject by resolving relative path else currentObject.parent 

         - **tagParentAbsolutePath** = parentObject.path+"/"+currentObject.parentPath      

         - **tagAbsolutePath** = absoluteParentPath+"/"+currentObject.path
         

if **tagAbsolutePath**  regex match any  absolute_ path of tag, value of this tag evaluated by type_



      
To explain with example:

.. code-block:: yaml

   result:
      path: /orders/order                              #path = orders/order
                                                          #parentPath = ""  
                                                          #tagAbsolutePath= /orders/order
                                                          #tagParentAbsolutePath= /                                   
      fields:                       
         defaultName:                                     #path =defaultName (default value is fieldName)
                                                          #parentPath = ""
                                                          #tagParentAbsolutePath =/orders/order (parent(result) absoluteTagPath))
                                                          #tagAbsolutePath =/orders/order/defaultName
         tagPathDefined:                       
            path: status                               #path =status
                                                          #parentPath = ""
                                                          #tagParentAbsolutePath =/orders/order (parent(result) absoluteTagPath))
                                                          #tagAbsolutePath =/orders/order/status
         tagPathAndParentPath:                    
               path: name                              #path = name
               parentPath: category                    #parentPath  = category
                                                          #tagParentAbsolutePath =/orders/order/category
                                                          #tagAbsolutePath =/orders/order/category/name
                     
         innerObject:             
              type: object             
              path: foo                                #path = "innerObject"   
                                                          #parentPath = ""
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/innerObject
              fields:     
                 normalPathInInnerObject:                 #path = "normalPathInInnerObject"
                                                          #parentPath = ""
                                                          #tagParentAbsolutePath =/orders/order/innerObject
                                                          #tagAbsolutePath =/orders/order/innerObjcet/normalPathInInnerObject
                 relativeTagPath:            
                       path:./defaultName              #path = ../defaultName    (only level up)
                                                          #parentPath = ""
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/defaultName
                 relativeParentPath:            
                       path:defaultName                #path = defaultName    
                       parentPath:../                  #parentPath = "../" (only level up)
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/defaultName                                          relativeTagPathAndParentPath:                
                       path:../defaultName             #path = defaultName    
                       parentPath:../                  #parentPath = "../" (only level up)  
                                                          #tagParentAbsolutePath =/orders/order
                                                          #tagAbsolutePath =/orders/order/../defaultName (relative path of parentPath important. path considered as regex)

                 relativePathFromResult:                
                   path:/orders/order/defaultName      #path = /orders/order/defaultName    
                                                          #parentPath = "" 
                                                          #tagParentAbsolutePath =/orders/order/defaultName    
                                                          #tagAbsolutePath =/orders/order     
  
  
  
  
      
**tagAbsolutePath and tagParentAbsolutePath**: 


.. csv-table::
    :header: fieldName, path, parentPath,tagAbsolutePath, tagParentAbsolutePath
    :stub-columns: 1
    :align: left
    :delim: |   
    
      result | orders/order |  | order/simpleOrder | /
      result | orders/order |  | order/simpleOrder | /
      defaultName | defaultName(default value is fieldName) | order/simpleOrder/defaultName | order/simpleOrder
      tagPathDefined | status | order/simpleOrder/status | order/simpleOrder
      tagPathAndParentPath | status | order/simpleOrder/category/name | order/simpleOrder/category
      innerObject | innerObject | order/simpleOrder/innerObject | order/simpleOrder
      normalPathInInnerObject | normalPathInInnerObject | order/simpleOrder/innerObject/normalPathInInnerObject | order/simpleOrder/innerObject
      relativeTagPath | defaultName | order/simpleOrder/defaultName | order/simpleOrder
      relativeParentPath | defaultName | order/simpleOrder/defaultName | order/simpleOrder
      relativeTagPathAndParentPath | defaultName | order/simpleOrder/..defaultName (relative path of parentPath important. path considered as regex) | order/simpleOrder
      relativePathFromResult | /orders/order/defaultName | order/simpleOrder/defaultName | order/simpleOrder
