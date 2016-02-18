# Contribute

## Versioning
This project adheres to [Semantic Versioning](http://semver.org/).


## Git
This project uses the Git layout described in the article [A successful
Git branching model](http://nvie.com/posts/a-successful-git-branching-model/)
by Vincent Driessen.

The latest release of this project can be found in the `master` branch,
tagged with the release version. The latest nightly can be found in the
`develop` branch. Features are added only to the `develop` branch,
whereas hotfixes are added to both the `develop` branch and the `master`
branch (which in turn results in a new release).


## Tests
Tests are in the `src/test/` folder. Almost all tests extend the
`SpoofaxCodeInsightFixtureTestCase` class, which set ups the test
 environment. The test data can be found in the `testdata/` folder.
 The test data is not in a source root, as it's not valid source
 and must not be compiled with the plugin.


## Development
### Run
Run this plugin in a sandbox IntelliJ IDEA instance using

```
gradle runIdea
```

or run the _IntelliJ Plugin_ configuration from IntelliJ.


### Debug
Debug the JPS plugin using 

```
gradle debugIdea
```

or run the _IntelliJ Plugin (Debug JPS)_ configuration from IntelliJ.
Then connect a debugger to port 5005, or debug the _JPS Plugin_
configuration from IntelliJ.


### Test
Run the tests using

```
gradle check
```

or run the _Unit Tests_ configuration from IntelliJ.


### Deploy
To deploy the plugin, click the _Build_ → _Prepare All Plugin Modules
For Deployment_ menu.
