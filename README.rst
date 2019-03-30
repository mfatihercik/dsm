

Introduction  
============
Declarative Stream Mapping(DSM) is a *stream* deserializer library that makes parsing of **XML and JSON** easy. 
DSM allows you to make custom parsing, filtering, 
transforming, aggregating, grouping on any 
JSON or XML document at stream time(read only once). 
DSM uses yaml or json for configuration definitions 

**If you parsing a complex, huge  file and 
want to have high performance and low memory usage then DSM is for you.**




Features
==============

- **Work** for both **XML** and **JSON** 
- **Custom stream parsing**
- **Filtering** by value on any field with very **low cognitive complexity**
- Flexible value **transformation**. 
- **Default value assignment**
- Custom **function calling** during parsing
- **Powerful Scripting** (`Apache JEXL <https://commons.apache.org/proper/commons-jexl/reference/syntax.html>`_, Groovy, Javascript and other jsr223 implementations are supported)
- **Multiple inheritance** between  DSM config file (DSM file can **extends to another config file**) 
- **Reusable fragments support** 
- Very **short learning curve**
- **Memory** and **CPU** efficient
- **Partial data extraction** from JSON or XML
- **String manipulation** with expression

==============
Installation
==============

**Jackson**

.. code-block:: xml

    <dependency>
      <groupId>com.github.mfatihercik</groupId>
      <artifactId>dsm</artifactId>
      <version>1.0.1</version>
    </dependency>


**Gradle**
      
.. code-block:: xml

   compile ('com.github.mfatihercik:dsm:1.0.1')

=============================================================
`Documentation <https://mfatihercik.github.io/dsm/>`_.
=============================================================


