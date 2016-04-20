# ACRHO client

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
	
