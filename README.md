# Excalibur

This project aims to provide a clean, easy access to data in Excel sheets. We had the need to access this data from Ant tasks and used Apache POI, but as we discussed moving some of those elements from Excel spreadsheet into a database we noted how tightly coupled to POI objects the whole code was. As such, we made a decision of transforming the code into hiding the access behind Maps and Lists, which makes it, in my opinion, much more transparent.

## Installation

Run: gradle jar

## Usage

TODO: Write usage instructions

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request.

## History

2016-12-12: Initial version.

## Dependencies

This project relies on some amazing projects:

* JUnit 4 (http://junit.org/junit4/)
* Logback (http://logback.qos.ch/)
* Apache POI (https://poi.apache.org/)

## Authors

* **Tiago Veiga Lázaro** - *Initial work* - [pistantan](https://github.com/pistantan)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
