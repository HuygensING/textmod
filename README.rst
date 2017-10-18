TopMod
======

A REST interface to Topic Modeling.

|stars| |pulls| |autom| |build|

.. |stars| image:: https://img.shields.io/docker/stars/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |pulls| image:: https://img.shields.io/docker/pulls/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |autom| image:: https://img.shields.io/docker/automated/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/
.. |build| image:: https://img.shields.io/docker/build/huygensing/topmod.svg
   :target: https://hub.docker.com/r/huygensing/topmod/builds/

Usage
-----

Edit ``config.yml`` as needed. Then compile and run TopMod itself::

  mvn clean package &&
    ./target/appassembler/bin/topmod server config.yml

or use the Dockerfile, ``docker run -p 8080:8080 -v model-vol:/models $(docker build -q .)``.
The docker volume ``model-vol`` will be created if it does not exist.
The mounted directory, ``/models``, must match the ``dataDirectory`` setting
in the configuration file.

Alternatively, you can pull the latest build from Dockerhub::

  docker pull huygensing/topmod

and run that version, ``docker run -p 8080:8080 -v model-vol:/models huygensing/topmod``.
This version also exposes various Dockerhub build properties at ``/about``::

  curl -s localhost:8080/about | jq

When the TopMod server starts, it will check the directory "bootstrap" on the volume
that is mapped (cf. -v model-vol:/models).
If a *.zip file is found, it will unzip the file and create a term index for internal
usage (for denormalization of suggestions). If this succeeds, the zip file is deleted.
Currently the zip file *must* contain a directory "model", as follows::

  model
  |
  |-- termvectors.bin
  |
  |-- terms
      |
      |-- terms-fr.txt
      |-- terms-la.txt
      |-- terms-nl.txt

When a topic model is present, search term suggestions can be optained as follows::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d '{"query":"Jupiter Saturnus"}'

 N.B. Topic model files can be uploaded in zipped form, bu this is now deprecated::

  curl -F "file=@model.zip;filename=model.zip" http://localhost:8080/models
 