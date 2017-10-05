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

Topic model files can be uploaded in zipped form::

  curl -F "file=@model.zip;filename=model.zip" http://localhost:8080/models

Once a topic model is uploaded, word suggestions can be optained as follows::

  curl -H "Content-Type: application/json" \
    http://localhost:8080/suggest -d 'Jupiter Saturnus'
