



def main():
    print "Admin Console"
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

    while(True)
        try:
            user_input = int(input("Enter a number: "))
            if(user_input > 0 and user_input < 17) break
            print("Try Again")
        except ValueError:
            print("Try Again")
    

    url = "localhost:3000/"

    if(user_input == 1):
        print "Input Country Name"
        name_input = input("Name: ")
        myResponse = requests.post(url+'/add/country', {'data_type':'country','name': name_input})
        if(myResponse.ok) print "Country created with success"
    elif(user_input == 2):
        print "Input Item Name"
        name_input = input("Name: ")
        myResponse = requests.post(url+'/information', {'data_type':'item','name': name_input})
        if(myResponse.ok) print "Item created with success"

    elif(user_input == 3):
        myResponse = requests.get(url + '/list/country')
        if(myResponse.ok):
            jData = json.loads(myResponse.content)
            for key in jData:
                print key + " : " + jData

        else:
            myResponse.raise_for_status()

    elif(user_input == 4):
        myResponse = requests.get(url + '/list/item')
        if(myResponse.ok):
            jData = json.loads(myResponse.content)
            for key in jData:
                print key + " : " + jData

        else:
            myResponse.raise_for_status()
            
    elif(user_input == 5):
        print "Input Item Name"
        name_input = input("Name: ")
        myResponse = requests.get(url + '/revenue', {'name': name_input})

    elif(user_input == 6):
        print "Input Item Name"
        name_input = input("Name: ")
        myResponse = requests.get(url + '/expense', {'name': name_input})
    elif(user_input == 7):
        print "Input Item Name"
        name_input = input("Name: ")
        myResponse = requests.get(url + '/profit', {'name': name_input})
    elif(user_input == 8):
        myResponse = requests.get(url + '/total/revenue')
    elif(user_input == 9):
            myResponse = requests.get(url + '/total/expense')
    elif(user_input == 10):
            myResponse = requests.get(url + '/total/profit')
    elif(user_input == 11):
    elif(user_input == 12):
    elif(user_input == 13):
    elif(user_input == 14):
    elif(user_input == 15):
    elif(user_input == 16):
    elif(user_input == 17):

