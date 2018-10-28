# Stock Watch

stockwatch.website

## Business problem and motivation
For NASDAQ and NYSE listed companies, good business reputation is very important.
Any misleading information regarding the company available online can have a bad impact on their image resulting into loss in business

Wikipedia is an online encyclopaedia and a large resource of information, created and edited by volunteers.
There are numbers of users who are frequent contributor to Wikipedia pages, few are genuine while few are biased towards the company image.

## Solution
To build a pipeline for analysing trends of edits done on Wikipedia pages of NASDAQ and NYSE listed companies 
Get insight into edits on the page:
- Users doing edits 
- Time series of edits 
- Revision history of the edited page at a given time

## Data
Crawling the wikipedia pages of NASDAQ and NYSE listed companies to get the basic information of the company and the history of edits on the page.
- Processing and storing the required information in JSON format
- Also storing the raw data as HTML pages 

## Pipeline

![alt text](https://github.com/SainathDutkar/StockWatch/blob/master/images/StockWatchPipeline.PNG)

- The Crawler is running on Amazon EC2 instance to mainly crawl NASDAQ and NYSE listed Wikipedia pages 
- The data extracted from the wikipedia pages is stored in the JSON format in S3 bucket along with the raw data as HTML pages
- The Spark job is run to batch process the JSON data and extract required results and store them to MySQL database 
- Dash is used as a visualization tool to query MySQL and show results 

## Tools and technologies used
 - Amazon EC2
 - Amazon S3
 - Apache Spark
 - Amazon RDS MySQL
 - Dash by Plotly
 
 ## 
