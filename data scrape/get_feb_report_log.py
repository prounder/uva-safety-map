import timing
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.common.exceptions import NoSuchElementException

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

def parse_description(desc):
	data = desc.split("–")
	return [data[0], data[-1]] #desc, location

def parse_date_time(datetime):
	data = datetime.split(" ")
	return [data[2], data[1]] #date, time

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

#driver.get("http://uvapolice.virginia.edu/crime-log-february-2018")
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

		data = raw_log.split("\n")
		if data[0].split(" ")[0] != "DAILY":
			date, time = parse_date_time(data[2])
			desc, loc = parse_description(data[0])
			parsed_data_file.write(date+"\t" + time+"\t" + loc+"\t" +desc+"\t" + "\n")

		counter(1, 1) #Testing

	print("Total number of logs: " + str(count))

except NoSuchElementException:
	print("No logs found for " + month + " " + year)
	raw_data_file.write("No logs found for " + month + " " + year)

raw_data_file.close()
parsed_data_file.close()

driver.close()

