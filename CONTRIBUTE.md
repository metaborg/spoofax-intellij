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
