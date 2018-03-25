import timing
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.common.exceptions import NoSuchElementException

import re #for data parsing

##########Functions##########

count = 0
def counter(inc, printInc):
	global count
	if printInc == 0:
		count = 0
		#print(count)
	else:
		count += inc
		#if count%(printInc) == 0:
			#print(count)
	return count

def parse_log_data(raw_log):
	data = raw_log.split("\n")
	date, time, desc, loc = "x", "x", "x", "x"

	if data[0].split(" ")[0] == "DAILY": #section header
		date = "*" + data[1]
	else:
		date, time = parse_date_time(data[2])
		desc, loc = parse_description(data[0])
	return [date, time, desc, loc]

def parse_date_time(datetime):
	#data = datetime.split(" ")
	data = re.split("[\s]+", datetime)
	#print(data)
	#date, time = "x", "x"
	date = data[2].strip()
	time = data[1].strip()
	return [date, time] #date, time

def parse_description(rawdata):
	data = re.split("[\–\-]", rawdata)
	#data = re.split("[^a-zA-Z0-9'\s]+", rawdata) #alphanumeric
	#print(data)
	if len(data) <= 1: return ["x", "x"]

	desc = data[0].strip()
	loc = find_address(data)
	return [desc, loc] #desc, location

def find_address(datalist):
	locdata = ""
	for string in datalist:
		string = string.strip()
		if re.match(r".*[0-9]$", string) != None:
			locdata = string
			#print(locdata)
			break
	if locdata == "": return "X"

	locdata = re.split("\s{2,}", string)
	loc = locdata[0].strip()
	return loc

##########Code##########

driver = webdriver.Chrome()

month = "February"
year = "2018"

raw_data_filename = month + "-" + year + "-raw.txt"
parsed_data_filename = month + "-" + year + "-parsed.txt"

raw_data_file = open(str(raw_data_filename), "w")
parsed_data_file = open(str(parsed_data_filename), "w")

raw_data_file.write("Crime Log: "  + month + " " + year + "\n")
parsed_data_file.write("Crime Log: "  + month + " " + year + "\n")
parsed_data_file.write("Date\tTime\tLocation\tDescription\n")


driver.get("http://uvapolice.virginia.edu/crime-log-" + month + "-" + year)

try:
	section = driver.find_element_by_class_name("field-type-text-with-summary")
	logs = section.find_elements_by_xpath(".//div/div/div/p")
	
	#Print all logs for month in year
	print("Printing logs for " + month + " " + year)
	counter(0, 0) #Testing
	for log in logs:
		raw_log = log.text
		raw_data_file.write(raw_log + "\n\n")

		data = parse_log_data(raw_log)
		date, time, desc, loc = data
		if date[0] != "*": #* indicating day header log entry
			parsed_data_file.write(date+"\t" + time+"\t" + loc+"\t" +desc+"\t" + "\n")

		counter(1, 1) #Testing

	print("Total number of logs: " + str(count))

except NoSuchElementException:
	print("No logs found for " + month + " " + year)
	raw_data_file.write("No logs found for " + month + " " + year)

raw_data_file.close()
parsed_data_file.close()

driver.close()

