TextMod
=======

A REST interface to Text Modeling Tools, currently including topic modeling and keyword modeling.

|stars| |pulls| |autom| |build|

.. |stars| image:: https://img.shields.io/docker/stars/huygensing/textmod.svg
   :target: https://hub.docker.com/r/huygensing/textmod/
.. |pulls| image:: https://img.shields.io/docker/pulls/huygensing/textmod.svg
   :target: https://hub.docker.com/r/huygensing/textmod/
.. |autom| image:: https://img.shields.io/docker/automated/huygensing/textmod.svg
   :target: https://hub.docker.com/r/huygensing/textmod/
.. |build| image:: https://img.shields.io/docker/build/huygensing/textmod.svg
   :target: https://hub.docker.com/r/huygensing/textmod/builds/

Usage
-----

Edit ``config.yml`` as needed. Then compile and run TextMod itself::

  mvn clean package &&
    ./target/appassembler/bin/textmod server config.yml

or use the Dockerfile, ``docker run -p 8080:8080 -v model-vol:/models $(docker build -q .)``.
The docker volume ``model-vol`` will be created if it does not exist.
The mounted directory, ``/models``, must match the ``dataDirectory`` setting
in the configuration file.

Alternatively, you can pull the latest build from Dockerhub::

  docker pull huygensing/textmod

and run that version, ``docker run -p 8080:8080 -v model-vol:/models huygensing/textmod``.
This version also exposes various Dockerhub build properties at ``/about``::

  curl -s localhost:8080/about | jq


Bootstrapping
~~~~~~~~~~~~~

When the TextMod server starts, it will check the directory ``bootstrap`` on the volume
that is mapped (cf. ``-v model-vol:/models``)::

  bootstrap
  |
  |-- keywords
  |
  |-- topics

The ``keywords`` directory is checked for keyword models; the ``topics`` directory is
checked for topic models. If a ``*.zip`` file is found in one of these directories, it
will be unzipped and processed to make the model ready for usage. If this succeeds,
the zip file is deleted.


Currently a keyword model file *must* contain a directory ``default``, as follows::

  default
  |
  |-- word-counts.csv

Currently a topic model file *must* contain a directory ``model``, as follows::

  model
  |
  |-- termvectors.bin
  |
  |-- terms
      |
      |-- terms-fr.txt
      |-- terms-la.txt
      |-- terms-nl.txt


Keywords
~~~~~~~~

The endpoint for determining keywords can be tested as follows::

  curl -X POST -H "Content-Type: application/xml" \
    http://localhost:8080/keywords -d @example.xml

Here ``example.xml`` can be any xml file. The project contains an ``example.xml``
containing a file of ePistolarium 2.0.


Topic models
~~~~~~~~~~~~

When a topic model is present, search term suggestions can be obtained as follows::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d '{"query":"Jupiter Saturnus"}'

N.B. Topic model files can be uploaded in zipped form, but this is now deprecated::

  curl -F "file=@model.zip;filename=model.zip" http://localhost:8080/models


Cocitation analysis
~~~~~~~~~~~~~~~~~~~

TextMod can perform a cocitation analysis for persons mentioned in a set of documents.
It can be used as follows:
  
  curl -X POST -H "Content-Type: application/json" \
    http://localhost:8080/cocit -d @documents.json

Here ``documents.json`` is a sample input document provided in the project.
