# ACRHO client

## Presentation

Client use to call web pages from [Acrho] (http://www.archo.org) and then parse html code to get informations:

- All runs
- Results by run
- Runner details

## Installation

### as a maven dependency

Copy this in pom.xml:

	<dependency>
		<groupId>org.acrho</groupId>
		<artifactId>acrho-client</artifactId>
		<version>1.0.0</version>
	</dependency>
	
## Usage

Get all runs (for current year):

	AcrhoService.getRuns();
	
Get results for a run:

	AcrhoService.getResults(runId);
	
Get runner details:

	AcrhoService.getRunner(runnerId);
	
## License

WTFPL-2.0 © Vincent Hullaert
	
