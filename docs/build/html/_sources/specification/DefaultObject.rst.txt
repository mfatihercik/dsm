_`Default Object`
==================

---------------------

Default Object determines how the default_ field is assigned.
   
Fields:
    .. csv-table::
      :header: Field Name, Type, Description
      :stub-columns: 1
      :delim: |
      
      value_ | string |  **REQUIRED** default value that is assigned to current field
      force  | string | Use the default value, even if the tag specified in the "path_" field is in the source file.
      atStart | string | assign default value at start of tag.

_`value`
------------

**REQUIRED**
it  holds default value that is assigned to current field
 
if the value starts with the "$" character, it is treated as "expression" and is resolved by expression resolver.  

The following objects are available in Expression Context.


.. csv-table::
    :header: Name, Data Type, Description, Example
    :stub-columns: 1
    :delim: |  
    
    params_ | Map<string,any> | params_ object. | **params.dateFormat** =='dd.MM.yyyy' 
    self_ | Node_ | current node object that hold data of current complex type_ | **self.data.foo** => foo field of current node,  **self.parent.data.foo** => foo field of parent node, **self.data.bar.foo** => foo field of bar object in current node.
    all_ | Map<string,Node_> | A map that stores all nodes by the "fieldName" of `Parsing Element Object`_  | **all.bar.data.foo** => foo field of **bar** node,  **all.barList.data[0].foo** => *foo* field of first item of *barList* node
    
_`force`
----------------


if it's value is true, it means Use the default value, 
even if the tag specified in the "path_" field is in the source file. 
if force value is true, default value is assigned both start and end of parentPath_. 
It is mostly used with filter field or with value in params_.
The default value is false.

_`atStart`
----------------

if atStart_ filed is true, default value is assigned at start of the tag. other wise default value is assigned at the end of the tag.

.. seealso::

   default_