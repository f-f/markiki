# Markiki

[![Build Status](https://travis-ci.org/ff-/markiki.svg?branch=master)](https://travis-ci.org/ff-/markiki)

[![Dependencies Status](http://jarkeeper.com/ff-/markiki/status.png)](http://jarkeeper.com/ff-/markiki)

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

`$ markiki [options] /path/to/markdown/files/`

#### Options

`-h, --help` : print out the help text

`-w, --watch` : watch the `src/` folder for changes

### Important things

- In the path you provide as a parameter to the program there should be a `src/` folder, containing your markdown files, nested as needed into folders (which will be your categories)
- Also there must **not** be an `out/` folder, as it will be created by the program, and your wiki will be put there. So your webserver should serve `/path/to/markdown/files/out/`
- Into the `/src` folder, any directory called `static` will be ignored, as the name is reserved for hosting static assets. (*Coming soon*)
- Your markdown files will have a big title (single #) that will become the article title, and parsed to become the article path into the wiki too.

## Hacking

- [Clone this repo](https://github.com/ff-/markiki) somewhere, then `cd markiki`
- Install java, [install lein](http://leiningen.org/#install)
- `./scripts/watch` to start the cljs autobuilding
- ...Make your changes...
- *(Tests coming soon!)*
- In another terminal, run `lein run -- /path/to/markdown/files` everytime you want an updated version, and point your browser to `file:///path/to/markdown/files/out/index.html` (or refresh the page)
- Alternatively, you can build also with `lein bin && target/markiki /path/to/markdown/files` etc.

## Bugs

For bugs, questions, comments, anything really, please open an [issue](https://github.com/ff-/markiki/issues)!

## Docker image

*Coming soon.*


## Contributors

See: https://github.com/ff-/markiki/graphs/contributors

