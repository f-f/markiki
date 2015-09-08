# Markiki

[![Build Status](https://travis-ci.org/ff-/markiki.svg?branch=master)](https://travis-ci.org/ff-/markiki)

[![Dependencies Status](http://jarkeeper.com/ff-/markiki/status.svg)](http://jarkeeper.com/ff-/markiki)

### Rationale

Have you ever woke up sweating in the middle of the night trying to recall the deploy procedure of an application you started two years ago on your server only to find out that you irreparably forgot it?

Have you ever been tired of having to fire up an instance of MediaWiki to document all your personal deploys/drafts/thoughts/recipes/< put-something-here >?

Have you ever *cried* while googling for some obscure MediaWiki syntax rule?

<hr>

Yeah, we all atleast once did the things listed up there.

But here comes **Markiki**, a *static markdown personal wiki!*


## Installation

Easy as:
- Install Java on your box. (openjdk7 is ok)
- [Grab the latest release](https://github.com/ff-/markiki/releases/latest) and put the `markiki` executable somewhere in your path. (e.g. `~/bin`)


## Usage

`$ markiki [options] /path/to/source/markdown/files/ /path/to/output/folder/`

Then serve the folder `/path/to/output/folder/` with your favourite webserver!

#### Options

`-h, --help` : print out the help text

`-w, --watch` : watch the source folder for changes

### Important things

- The source path you provide as a parameter to the program will containin your markdown files, nested as needed into folders (which will be your categories)
- Your webserver should serve `/path/to/output/folder/`
- Into the source folder, any directory called `static` will be ignored, as the name is reserved for hosting static assets. (*Coming soon*)
- Your markdown files in the source folder will have `.md` extension, a big title (single #) that will become the article title, and their filesystem name will become the article paths into the wiki.

## Hacking

- [Clone this repo](https://github.com/ff-/markiki) somewhere, then `cd markiki`
- Install java, [install lein](http://leiningen.org/#install)
- `./scripts/watch` to start the cljs autobuilding (or `./scripts/build` for a one-time build)
- ...Make your changes...
- *(Tests coming soon!)*
- In another terminal, run `lein run -- /path/to/source/markdown/files/ /path/to/output/folder/` everytime you want an updated version, and point your browser to `file:///path/to/output/folder/index.html` (or refresh the page)
- Alternatively, you can build also with `lein bin && target/markiki /path/to/source/markdown/files/ /path/to/output/folder/` etc.

## Bugs

For bugs, questions, comments, anything really, please open an [issue](https://github.com/ff-/markiki/issues)!

## Docker image

`docker run --name $CONTAINER_NAME -v $SOURCE_PATH:/home/markiki -p $HOST_BIND_IP_PORT:80 -d nilrecurring/markiki`


In this case the variable `$HOST_BIND_IP_PORT` should be in the form `IP:PORT` (e.g. `0.0.0.0:80`)

To build from the local Dockerfile just `docker build -t nilrecurring/markiki .`


## Contributors

See: https://github.com/ff-/markiki/graphs/contributors

