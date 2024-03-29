
import json
import requests

def main():
    print "1  --> Add Country"
    print "2  --> Add Item"
    print "3  --> List Countries"
    print "4  --> List Items"
    print "5  --> Get Revenue per item"
    print "6  --> Get Expense per item"
    print "7  --> Get Profit per item"
    print "8  --> Get total Revenue"
    print "9  --> Get total Expense"
    print "10 --> Get total Profit"
    print "11 --> Get avg purchase(separated by item)"
    print "12 --> Get avg purchase(aggregated by item)"
    print "13 --> Get item with Highest Profit"
    print "14 --> Get Total revenue last hour"
    print "15 --> Get Total expenses last hour"
    print "16 --> Get Total profit last hour"
    print "17 --> Get Country with Highest Sale per item"
    print "18 --> Quit"
    while(True):
        print "Admin Console"
        while(True):
            try:
                user_input = int(raw_input("Enter a number: "))
                if(user_input > 0 and user_input <= 18): break
                print "Try Again"
            except ValueError:
                print "Try Again"
        

        url = "http://127.0.0.1:3000"
        
        if(user_input == 1):
            print "Input Country Name"
            name_input = raw_input("Name: ")
            myResponse = requests.post(url+'/information', {'data_type':'country','name': name_input})
            if(myResponse.ok): print "Country created with success"
            else: print "something went wrong"

        elif(user_input == 2):
            print "Input Item Name"
            name_input = raw_input("Name: ")
            myResponse = requests.post(url+'/information', {'data_type':'item','name': name_input})
            if(myResponse.ok): print "Item created with success"
            else: print "something went wrong"

        elif(user_input == 3):
            myResponse = requests.get(url + '/list/country')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"]
            else:
                myResponse.raise_for_status()

        elif(user_input == 4):
            myResponse = requests.get(url + '/list/item')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"] 
            else:
                myResponse.raise_for_status()



        elif(user_input == 5):
            myResponse = requests.get(url + '/results/revenue')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"]," ",key["value"]  

            else:
                myResponse.raise_for_status()

        elif(user_input == 6):
            myResponse = requests.get(url + '/results/expense')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"]," ",key["value"]  
    
            else:
                myResponse.raise_for_status()
        elif(user_input == 7):
            myResponse = requests.get(url + '/results/profit')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"]," ",key["value"]  
            else:
                myResponse.raise_for_status()
        elif(user_input == 8):
            myResponse = requests.get(url + '/results/total_revenue')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Revenue: ",jData["value"]
                        
            else:
                myResponse.raise_for_status()
        elif(user_input == 9):
            myResponse = requests.get(url + '/results/total_expense')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Expense: ",jData["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 10):
            myResponse = requests.get(url + '/results/total_profit')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Profit: ",jData["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 11):
            myResponse = requests.get(url + '/results/mean_per_item')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                for key in jData:
                    print key["name"]," ",key["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 12):
            myResponse = requests.get(url + '/results/mean_per_purchase')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Purchase Mean: ",jData["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 13):
            myResponse = requests.get(url + '/results/highest_profit')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Highest Profit: ",jData["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 14):
            myResponse = requests.get(url + '/results/total_revenue_window')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Revenue Window: ",jData["value"]
            else:
                myResponse.raise_for_status()
        elif(user_input == 15):
            myResponse = requests.get(url + '/results/total_expense_window')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)
                print "Total Expense Window: ",jData["value"] 
            else:
                myResponse.raise_for_status()
        elif(user_input == 16):
            myResponse = requests.get(url + '/results/total_profit_window')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)          
                print "Total Profit Window: ",jData["value"]
            else:
                myResponse.raise_for_status()


        elif(user_input == 17):
            myResponse = requests.get(url + '/results/highest_sales')
            if(myResponse.ok):
                jData = json.loads(myResponse.content)    
                for key in jData:
                    print key["name"], " ", key["country_name"], " ", key["value"]      
            else:
                myResponse.raise_for_status()
        elif(user_input == 18):
            return


main()