#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2 
import json
import urllib2
import urllib
from google.appengine.api import urlfetch
from google.appengine.ext import ndb 
from google.appengine.api import users
from datetime import datetime


# Consts for the update method
ACTION_JOIN_CHANNEL =5 
ACTION_ADD_CHANNEL = 4
ACTION_SEND_MESSAGE = 3 
ACTION_LEAVE_CHANNEL = 6
ACTION_LOGIN_CHANNEL = 1
ACTION_DELETE_CHANNEL = 7
ACTION_LOGOFF_CHANNEL = 2

# Saves my server link
MY_LINK = "http://ap-server.appspot.com"

# Methods
METHOD_POST = urlfetch.POST
METHOD_GET = urlfetch.GET




# Class Represents user in the data base
class User(ndb.Model):
	nickname = ndb.StringProperty()
	link = ndb.StringProperty()
	lastSeen = ndb.DateTimeProperty()
	isChange = ndb.BooleanProperty()
	

	
# Class represents channel in the data base
class Channel(ndb.Model):
	name = ndb.StringProperty() 
	channel_id = ndb.StringProperty()
	icon = ndb.StringProperty()
	isMine = ndb.BooleanProperty()

# This class represents the relation between Users and Channels
class ChannelUser(ndb.Model):
	user = ndb.KeyProperty(kind = User) 
	channel = ndb.KeyProperty(kind = Channel)
	
	
# This class help to convert channel to JSON
class Channel_JSON:
	def __init__(self, name ,channel_id, icon):
		self.channel_id = channel_id 
		self.icon = icon 
		self.name = name

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# This class converts channel id to JSON
class Channel_id_JSON:
	def __init__(self,channel_id):
		self.channel_id = channel_id

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# This class converts message to json without the sender
class Message_Json:
	def __init__(self,channel,text,longtitude,latitude):
		self.channel = channel
		self.text = text
		self.longtitude = longtitude
		self.latitude = latitude

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)


# This class converts message to json with the sender
class Message_User_Json:
	def __init__(self,channel_id,user_id,text,longtitude,latitude):
		self.channel_id = channel_id
		self.user_id = user_id
		self.text = text
		self.longtitude = longtitude
		self.latitude = latitude

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# This class converts server to json
class Server_JSON:
	def __init__(self,server):
		self.server = server

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)


# Class inverts channels to JSON
class Channels(ndb.Model):
	def __init__(self, channels):
		self.channels = channels
	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# Class Converts Network to JSON
class Network(ndb.Model):
	def __init__(self, channels):
		self.channels = channels
	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# Class converts members to JSON
class Members(ndb.Model):
	def __init__(self, channel_id,members):
		self.id = channel_id
		self.members = members
	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)


# Class that inverts list of channel to JSON
class Messages(ndb.Model):
	def __init__(self, messages,link):
		self.messages = messages
		self.change_server = link
	def __init__(self, messages):
		self.messages = messages
	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

class Servers(ndb.Model):
	def __init__(self, servers):
		self.servers = servers
	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)


# Class represents message the the data base
class Message(ndb.Model):
	user = ndb.KeyProperty(kind = User) 
	channel = ndb.KeyProperty(kind = Channel)
	text = ndb.TextProperty()
	longtitude = ndb.FloatProperty()
	latitude = ndb.FloatProperty()
	datetime = ndb.DateTimeProperty(auto_now_add = True)


class MessageRead(ndb.Model):
	message = ndb.KeyProperty(kind = Message)
	user = ndb.KeyProperty(kind = User)
	isRead = ndb.BooleanProperty()

# Class represents server in the data base
class Server(ndb.Model):
	link = ndb.StringProperty()

# Class that converts responces to JSON
class R_Msg():
	def __init__(self,status,message):
		self.status = int(status)
		self.message = message

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# Class of the register action
class Register(webapp2.RequestHandler): 
	def post(self):
		# Selects all of the servers
		query = ndb.gql("""SELECT * FROM Server""")
		# If there are more than 3 servers
		
		if query.count()>=3:
		    m = R_Msg(0,'Full')
		# Less than 3 servers
		else:
		    key = ndb.Key('Server', self.request.get('link'))
                    if key.get() is None:
                        server = Server(id = self.request.get('link'),link = self.request.get('link'))
                        server.put()
                        m = R_Msg(1,'')
                    else:
                        m = R_Msg(0,'Already Linked')           
                self.response.write(m.to_JSON())


	
# Unregister action
class Unregister(webapp2.RequestHandler):
	def post(self):	
		# deleting the show from the server table
		key = ndb.Key('Server', self.request.get('link'))
		if key.get() is None:
			m = R_Msg(0,'Not Linked')
		else:
			key.delete()
			m = R_Msg(1,"")
		self.response.write(m.to_JSON())		
			
# Main Handler (debugging)
class MainHandler(webapp2.RequestHandler):
    def get(self):
    	pass
		

# UserLogin action - login thorugh browser == debugging
class UserBLogin(webapp2.RequestHandler):
    def get(self):
    	# gets the current user
        user = users.get_current_user()
        if user:
            greeting = ('Welcome, %s! (<a href="%s">sign out</a>)' %
                        (user.nickname(), users.create_logout_url('/')))
            self.response.out.write('<html><body>%s</body></html>' % greeting)
        else:
        	# sign in for debuggin through browser
            greeting = ('<a href="%s">Sign in or register</a>.' %
                        users.create_login_url('/'))

        #self.response.out.write('<html><body>%s</body></html>' % greeting)
        user = users.get_current_user()
        if user:
        	self.response.out.write('user')
       		f_addUser(user.nickname(),link = MY_LINK)
       		f_update_all(user.nickname(),ACTION_LOGIN_CHANNEL,f_server_JSON(MY_LINK))
       		m = R_Msg('1','Succeeded')
                removeChannelsToOtherServers()
       	else:
       		m = R_Msg('0','failed')
	   		
   			
			
		
		self.response.out.write('<html><body>%s</body></html>' % greeting)

# Login function
class UserLogin(webapp2.RequestHandler):
    def get(self):
    	# Check if the user exist
    	user = users.get_current_user()
        if user:
        	# check if the user already in the system
        	if not f_isUserExist(user.nickname()):
        		f_addUser(user.nickname(),link = MY_LINK)
       			f_update_all(user.nickname(),ACTION_LOGIN_CHANNEL,f_server_JSON(MY_LINK))
        		m = R_Msg('1','')
                        removeChannelsToOtherServers()
    			
        	else:
        		m = R_Msg('0','User Already log in')
       	else:
       		m = R_Msg('0','There is no google user API and Meydan is temble')

       	self.response.write(m.to_JSON())
 
# SaveUser action - for debugging
class SaveUser(webapp2.RequestHandler):
	def get(self):
		# getting the currect user
		user = users.get_current_user()
		if user:
			# Creating user instance
			user = User(id = user.nickname(),nickname = user.nickname(),link=MY_LINK)
			# Save in the user table
			user.put()


			# Responding
			m = R_Msg('1','Succeeded')
			self.response.write(m.to_JSON())
			
		else:
			# Responding
			m = R_Msg('1','User not exist')
			self.response.write(m.to_JSON())

# UserLogOf action
class UserLogoff(webapp2.RequestHandler):
	def get(self):
		user = users.get_current_user()
		# If the user is in
		if user:
			# Checking if the user is in the system
			if f_isUserExist(user.nickname()):
				# Deleting from the user table
				key = ndb.Key('User', user.nickname()) 
				key.delete()
				# Updating all
				f_update_all(user.nickname(),ACTION_LOGOFF_CHANNEL,f_server_JSON(MY_LINK))
				m = R_Msg('1','')
			else:
				# The user is not log int
				m = R_Msg('0','The user is not logged in')
			#self.redirect(users.create_logout_url(self.request.uri))
			# Sending response
			

			self.response.write(m.to_JSON())

			
		else:
			# Responding for error
			m = R_Msg('0','User not exist')
			self.response.write(m.to_JSON())


# GetMyChannels action
class GetMyChannels(webapp2.RequestHandler):
	def get(self):
		# Select all the channels from the channel table where they are old by me
		query = ndb.gql("""SELECT * FROM Channel WHERE isMine = True""")
		channels = []
		# For each channel adding it to a channel array
		for channel in query:
			channels.append(Channel_JSON(name = channel.name,icon = channel.icon , channel_id = channel.channel_id))

		# Convert to JSON and sent it as a responde
		chans = Channels(channels)
		self.response.out.write(chans.to_JSON())

# GetNUmOfClients action
class GetNumOfClients(webapp2.RequestHandler):
	def post(self):
		# Note if chanel does not exist returns 0
		channel_id = self.request.get('id')
		self.response.out.write(f_getNumOfClient(channel_id))

# Update action
class Update(webapp2.RequestHandler):
	def post(self):
		# Getting the user the action and the dat
		user_name = self.request.get('user')
		action = int(self.request.get('action'))
		data = self.request.get('data')
		# Writs the data (debugging)
		
		
		data_arr = json.loads(data)

		
		flag = 0

		# Doing the right action while avoiding infinte loops
		# Becouse of the recursice update
		if(action == 1 and not (f_isUserExist(user_name))):
			# Adding user
			f_addUser(user_name,data_arr['server'])
			flag = 1

		elif(action == 2 and f_isUserExist(user_name)):
			# Deleting user
			f_delUser(user_name)
			flag = 1

		elif(action == 3):
			# Adding a message to the Message table
			f_addMessage(user_name,data_arr['channel'],data_arr['text'],data_arr['longtitude'],latitude=data_arr['latitude'])
			flag = 1

		elif(action == 4 and f_isChannelExist(data_arr['channel_id'])):
			# Adding a channel to the channel table
			f_addChannel(data_arr['channel_id'],data_arr['name'],data_arr['icon'],False)
			flag = 1
		
		elif(action == 5 and not (f_isChannelUserExist(data_arr['channel_id'],user_name))):
			# Adding channelUser to the channelUser table
			f_addChannelUser(user_name,data_arr['channel_id'])
			flag = 1
			
		elif(action == 6 and f_isChannelUserExist(data_arr['channel_id'],user_name)):
			# Deleting channel user from the channel user table
			f_delChannelUser(user_name,data_arr['channel_id'])
			flag =1
			
		elif(action == 7 and not (f_isChannelUserExist)):
			# Deleting Channel
			f_delChannl(data_arr['channel_id'])
			flag = 1
			
		# If the flag is equal to 1 than we have changed something
		if flag==1:
			f_update_all(user_name,action,data)

		# Writing response
		m = R_Msg('1','')
		self.response.write(m.to_JSON())


# Adding channel action
class AddChannel(webapp2.RequestHandler):
	def post(self):
		# Getting the user the action and the dat
		name = self.request.get('name')
		channel_id = self.request.get('id')
		icon = self.request.get('icon')
		user = users.get_current_user()
		# If the user exists
		if user:
			# If the user loged in
			if f_isUserExist(user.nickname()):

				# If the channel is already exist
				if f_isChannelExist(channel_id):
					m = R_Msg('0','Channel Already Exists')

				else:
					# elce adding the channel
					f_addChannel(channel_id,name,icon,True)
					f_update_all(user.nickname(),ACTION_ADD_CHANNEL,f_channel_JSOM(channel_id,name,icon))
					# Then adding the current user to the channel
					f_addChannelUser(user.nickname(),channel_id)
					f_update_all(user.nickname(),ACTION_JOIN_CHANNEL,f_channel_id_JSOM(channel_id))
					m = R_Msg('1','')
			else:
				m = R_Msg('0','User is log off')
				
		else:
			m = R_Msg('0','User is disconnected')

		# Responsing
		self.response.write(m.to_JSON())

# Joining Channel
class JoinChannel(webapp2.RequestHandler):
	def post(self):
		# Get the current user
		user = users.get_current_user()
		# If the user is in
		if user:
			channel_id = self.request.get('id')
			channel_key = ndb.Key('Channel',channel_id)
			if f_isUserExist(user.nickname()):

				# Checking if channel exist
				if f_isChannelExist(channel_id):
					# Checing if the user already sign to the channel
					if not(f_isChannelUserExist(channel_id,user.nickname())):
						f_addChannelUser(user.nickname(),channel_id)
						f_update_all(user.nickname(),ACTION_JOIN_CHANNEL,f_channel_id_JSOM(channel_id))
						m = R_Msg('1','')
					else:
						m = R_Msg('0','User is Already in the Channel')
				else:
					m = R_Msg('0','Channel does not exist')
			else:
				m = R_Msg('0','User is disconnected')

		else:
			m = R_Msg('0','User does not  exist!')
		
		self.response.write(m.to_JSON())

# Sending message
class SendMessage(webapp2.RequestHandler):
	def post(self):
		# Getting String channel_id, String text, Double longtitude, Double latitude
		channel_id = self.request.get('channel_id')
		text = self.request.get('text')
		longtitude = self.request.get('longtitude')
		latitude = self.request.get('latitude')

		# Getting the currect user
		user = users.get_current_user()
		# If the user is in
		if user:

			# If the user loged is
			if f_isUserExist(user.nickname()):

				# If the channel exists
				if f_isChannelExist(channel_id):
					# Adding new message to the data base
					f_addMessage(user.nickname(),channel_id,text,longtitude,latitude)

					m = R_Msg('1','')

					# Updating the other servers
					f_update_all(user.nickname(),ACTION_SEND_MESSAGE,f_message_JSON(channel_id,text,latitude,latitude))
				else:
					m = R_Msg('0','Channel does not exist')
			else:
				m = R_Msg('0','User is disconnected!')
		else:
			m = R_Msg('0','User does not exist')

		# Responsing
		self.response.write(m.to_JSON())

# Leaving Channel
class LeaveChannel(webapp2.RequestHandler):
	def post(self):
		# Getting the currecnt user and the id request
		channel_id = self.request.get('id')
		user = users.get_current_user()
		# if the user exists
		if user:
			# If the user loged is
			if f_isUserExist(user.nickname()):

				# if the channel exists
				if f_isChannelExist(channel_id):

					# if the channel user exist
					if f_isChannelUserExist(channel_id,user.nickname()):
						# Deleting channel
						f_delChannelUser(user.nickname(),channel_id)
						# Updating others
						f_update_all(user.nickname(),ACTION_LEAVE_CHANNEL,f_channel_id_JSOM(channel_id))
						
						# If the channel is empty now
						if f_getNumOfClient == 0:
							# Delete channel
							f_delChannl(channel_id)
							# Updating others
							f_update_all(user.nickname(),ACTION_DELETE_CHANNEL,f_channel_id_JSOM(channel_id))
						
						m = R_Msg('1','')
						

					else:
						m = R_Msg('0','User does not belong to channel')

						
				else:
					m = R_Msg('0','Channel does not exist')
			else:
				m = R_Msg('0','User is disconnected!')
		
		else:
			m = R_Msg('0','User does not  exists!')

		# Responsing
		self.response.write(m.to_JSON())

class Redirect(webapp2.RequestHandler):
	def get(self):
		self.response.write('hey')

# Getting servers
class GetServers(webapp2.RequestHandler):
	def get(self):
		# Select all the channels from the channel table
		query = ndb.gql("""SELECT * FROM Server""")
		servers = []
		# For each channel adding it to a channel array
		for s in query:
			servers.append(Server_JSON(server = s.link))

		# Convert to JSON and sent it as a responde
		srvs = Servers(servers)
		self.response.out.write(srvs.to_JSON())
		
		# Asking our 3 known servers
		# form_fields = {	}
		# query = ndb.gql("""SELECT * FROM Server""")
		# dic = dict()
		# dic['servers'] = []
		# for s in query:
		# 	# Unions servers list
		# 	result = f_send_request(s.link,"getServers",form_fields,METHOD_GET)
		# 	dic1 = json.loads(str(result))
		# 	dic = f_union_dict_list(dic,dic1)
			
			
		
		#self.response.write(json.dumps(str(dic)))

# Getting chennls
class GetChannels(webapp2.RequestHandler):
	def get(self):
		# Select all the channels from the channel table
		query = ndb.gql("""SELECT * FROM Channel""")
		channels = []
		# For each channel adding it to a channel array
		for channel in query:
			channels.append(Channel_JSON(name = channel.name,icon = channel.icon , channel_id = channel.channel_id))

		# Convert to JSON and sent it as a responde
		chans = Channels(channels)
		self.response.out.write(chans.to_JSON())

# Getting chennls
class getNetwork(webapp2.RequestHandler):
	def get(self):
		# Select all the channels from the channel table
		query = ndb.gql("""SELECT * FROM Channel""")
		network = []
		# For each channel adding it to a channel array
		for channel in query:
			members = []
			channel_id = channel.channel_id
			query_members = ndb.gql("""SELECT * FROM ChannelUser""")
			query = ndb.gql("""SELECT * FROM ChannelUser""")
			for channelUser in query:
				if channelUser.channel.get().channel_id == channel_id:
					members.append(channelUser.user.id())
			m = Members(channel_id,members)
			network.append(m)	
				# == channel_id and channelUser.user.id() == user_name:
		
			

		# Convert to JSON and sent it as a responde
		net = Network(network)
		self.response.out.write(net.to_JSON())

# Gets updates
class GetUpdates(webapp2.RequestHandler):
	def get(self):
		user = users.get_current_user()
		# if the user exists
		#self.response.out.write("*******test******\n")
		if user:
			key = ndb.Key('User', user.nickname()) 
			# Checking if the user loged in
			if f_isUserExist(user.nickname()):
				# Select all the Messages from the messages table
				query = ndb.gql("""SELECT * FROM Message""")
				messages = []
				# For each channel adding it to a channel array
				for m in query:
					if m.datetime > key.get().lastSeen:
						# self.response.out.write(m)
						# self.response.out.write("\n")
						# self.response.out.write(m.channel)
						# self.response.out.write("\n")
						# #key = ndb.Key('Channel',m.channel)

						channel_id = m.channel.get().channel_id
						messages.append(Message_User_Json(channel_id,m.user.id(),m.text,m.longtitude,m.latitude))

				# Convert to JSON and sent it as a responde
				'''key.get().lastSeen = datetime.now()
				key.get().put()'''
				entity = key.get()
				entity.lastSeen = datetime.now()
				mss = Messages(messages)
				#if we need to change the server because we moved this client
				if (entity.isChange):
                                    link = entity.link
                                    entity.isChange = False
                                    mss = Messages(messages,link)
                                entity.put()
				
			else:
				mss = R_Msg('0','User is disconnected!')
		else:
			mss = R_Msg('0','User is disconnected!')

		# Responsing
		self.response.out.write(mss.to_JSON())

class ChangeChannels(webapp2.RequestHandler):
        def post(self):
                ans = 0
                channels_str = self.request.get('remove')
                link_to_ser = self.request.get('linkToServer')
                channels_lst = channels_str.split(",")
                #summing the number of clients that will be transfered to
                #us if we took those channels
                clients = 0
                for ch_id in channels_lst:
                        form_fields = {
                            "id": ch_id
                        }
                        form_data = urllib.urlencode(form_fields)
                        result = urlfetch.fetch(url=link_to_ser+'/getNumOfClient',
                            payload=form_data,
                            method=urlfetch.GET,
                            headers={'Content-Type': 'application/x-www-form-urlencoded'})
                        clients += int(result)
                #getting the number of users currently connected to each server
                #(our server and the other one)
                query = ndb.gql("""SELECT * FROM User WHERE link = :serLink """,serLink = MY_LINK)
		myClients = query.count()
                avg = f_avgNumOfClients()
		#deciding if by accepting we will get a more balanced network by
		#checking if the change will bring us close to the average
                query = ndb.gql("""SELECT * FROM Server""")
                numOfSer = query.count()
                if (myClients + clients <= avg + numOfSer):
                    ans = 1
		m = R_Msg(ans,'')
                self.response.out.write(m.to_JSON())
                if ans == 0:
                    return
                #if we accepted, updating that those channel are ours
                for ch_id in channels_lst:
                    key = ndb.Key('Channel',ch_id)
                    entity = key.get()
                    entity.isMine = True
                    entity.put()

def removeChannelsToOtherServers():
        avg = f_avgNumOfClients()
        #check if we have more than the average amount of clients
        query = ndb.gql("""SELECT * FROM User WHERE link = :serLink """,serLink = MY_LINK)
	myClients = query.count()
        query = ndb.gql("""SELECT * FROM Server""")
        numOfSer = query.count()
	if (myClients <= avg + numOfSer):
            return
        #getting the other servers
	query = ndb.gql("""SELECT * FROM Server""")
        servers = []
	for s in query:
		servers.append(Server_JSON(server = s.link))
        #choosing which channels to change
        gaveCh = 0
        for link_to_ser in servers:
            for ch_id in channels_lst:
                #checking if it makes sense to move the channel to the other server
                form_fields = {
                    "id": ch_id
                }
                form_data = urllib.urlencode(form_fields)
                result = urlfetch.fetch(url=link_to_ser+'/getNumOfClient',
                    payload=form_data,
                    method=urlfetch.GET,
                    headers={'Content-Type': 'application/x-www-form-urlencoded'})
                otherClientsInCh = int(result)
                myClientsInCh = f_getNumOfClient(ch_id)
                #if it does, asking him to get the channel
                if myClientsInCh > 0 and myClientsInCh < otherClientsInCh:
                    form_fields = {
                        "id": ch_id
                    }
                    form_data = urllib.urlencode(form_fields)
                    result = urlfetch.fetch(url=link_to_ser+'/changeChannels',
                        payload=form_data,
                        method=urlfetch.POST,
                        headers={'Content-Type': 'application/x-www-form-urlencoded'})
                    jason = json.loads(resault)
                    ans = jason["status"]
                    #if we gave one channel it means we should be balanced
                    if (ans == 1):
                        f_agreedToTakeChannel(ch_id,link_to_ser)
                        gaveCh = 1
                        return
        #if we didn't give any channel we will try again with all of
        #the channels because we are currently not balanced
        for ch_id in channels_lst:
            form_fields = {
                "id": ch_id
            }
            form_data = urllib.urlencode(form_fields)
            result = urlfetch.fetch(url=link_to_ser+'/changeChannels',
                payload=form_data,
                method=urlfetch.POST,
                headers={'Content-Type': 'application/x-www-form-urlencoded'})
            jason = json.loads(resault)
            ans = jason["status"]
            #if we gave one channel it means we should be balanced
            if (ans == 1):
                f_agreedToTakeChannel(ch_id,link_to_ser)
                gaveCh = 1
                return
                
            

        
# function for adding data for table

def f_agreedToTakeChannel(ch_id,link):
	query = ndb.gql("""SELECT user FROM ChannelUser WHERE channel = :channel""", channel = ch_id)
	#------------------i am not sure if it gives me the users name or i need to do something
	#before that-------------------------------
	#marking that we need to update those users (in getUpdates)
	#that they need to change server
	for us in query:
            key = ndb.Key('User', us.id)
            entity = key.get()
            entity.isChange = True
            entity.link = link
            entity.put()
            
        

#getting the average number of clients per server
def f_avgNumOfClients():
        query = ndb.gql("""SELECT * FROM User""")
        usersNum = query.count()
        query = ndb.gql("""SELECT * FROM Server""")
        serversNum = query.count()
        avg = usersNum / (serversNum + 1)
        return avg
    

# Add user
def f_addUser(user_name,link):
	user = User(id = user_name,nickname = user_name,link = link,lastSeen = datetime.now(),isChange = False)
	user.put()
	pass
# Delete user
def f_delUser(user_name):
	key = ndb.Key('User', user_name) 
	key.delete()

# Add Message
def f_addMessage(user_name,channel_id,text,longtitude,latitude):
	user_key = ndb.Key('User', user_name)
	channel_key = ndb.Key('Channel',channel_id)
	message = Message(user = user_key,channel = channel_key,text = text,
		longtitude=float(longtitude),latitude=float(latitude))
	message.put()
	#f_addMessageRead(message.key,user_key)

# Add Channel
def f_addChannel(channel_id,name,icon,isMine):
	channel = Channel(id =channel_id, channel_id = channel_id,name = name, icon = icon,isMine = isMine)
	channel.put()

# Add chanel user
def f_addChannelUser(user_name,channel_id):
	channel_key = ndb.Key('Channel',channel_id)
	user_key = ndb.Key('User', user_name)
	channelUser = ChannelUser(user =  user_key,channel = channel_key)
	channelUser.put()

# Delete channel user
def f_delChannelUser(user_name,channel_id):
	channel_key = ndb.Key('Channel',channel_id)
	user_key = ndb.Key('User', user_name)
	query = ndb.gql("""SELECT * FROM ChannelUser WHERE channel = :channel AND user = :user"""
		,channel = channel_key,user = user_key)
	channel_key.delete()

	for channelUser in query:
		channelUser.key.delete()

# Deleting channel
def f_delChannl(channel_id):
	channel_key = ndb.Key('Channel',channel_id)

	query = ndb.gql("""SELECT * FROM ChannelUser WHERE channel = :channel""",channel = channel_key)
	for channelUser in query:
		channelUser.key.delete()

	query = ndb.gql("""SELECT * FROM Message WHERE channel = :channel """,channel = channel_key)
	for m in query:
		m.key.delete()

	channel_key.delete()

# 
def f_isChannelUserExist(channel_id,user_name):
	query = ndb.gql("""SELECT * FROM ChannelUser""")
	count = 0
	for channelUser in query:
		if channelUser.channel.get().channel_id == channel_id and channelUser.user.id() == user_name:
			count = count+1

	return count>0

def f_isChannelExist(channel_id):
	query = ndb.gql("""SELECT * FROM Channel WHERE channel_id = :channel_id """,channel_id = channel_id)
	return query.count()>0

def f_isUserExist(user_name):
	query = ndb.gql("""SELECT * FROM User WHERE nickname = :name """,name = user_name)
	return query.count()>0

def f_isMessageExists(channel,text,longtitude,latitude):
	query = ndb.gql("""SELECT * FROM Message WHERE text = :text and longtitude = :longtitude and latitude = :latitude """,text = text,longtitude=longtitude,latitude = latitude)
	return query.count()>0

def f_addMessageRead(message,user,isRead):
	messageRead = MessageRead(message = message,user = user,isRead = isRead)
	messageRead.put()


def f_signAsRead(message,user_name):
	user_key = ndb.Key('User', user_name)
	query = ndb.gql("""SELECT * FROM MessageRead WHERE  user = :user"""
		,user = user_key)

	for message in query:
		message.isRead = True
		message.put()

def f_getNumOfClient(chan_id):
	query = ndb.gql("""SELECT * FROM ChannelUser""")
	count = 0
	for channel in query:
		if channel.channel.get().channel_id == chan_id:
			count = count+1

	return count
	pass


# Function that converts virables to JSON

# Converts channelid to json
def f_channel_id_JSOM(channel_id):
	c = Channel_id_JSON(channel_id)
	return c.to_JSON()

# Converts channel to JSON
def f_channel_JSOM(channel_id,name,icon):
	c = Channel_JSON(channel_id,name,icon)
	return c.to_JSON()

# Converts message to JSON
def f_message_JSON(channel,text,longtitude,latitude):
	m = Message_Json(channel,text,longtitude,latitude)
	return m.to_JSON()

# Converts server to JSON
def f_server_JSON(link):
	s = Server_JSON(link)
	return s.to_JSON()

# Send an update to a specific server
def f_update(url,user,action,data):
	#url = 'http://localhost:12100/'
	form_fields = {
	  "user": user,
	  "action": action,
	  "data": data
	}
	form_data = urllib.urlencode(form_fields)
	# sending
	result = urlfetch.fetch(url=url+'/'+'update',
	    payload=form_data,
	    method=urlfetch.POST,
	    headers={'Content-Type': 'application/x-www-form-urlencoded'})


# Send an update to all servers
def f_update_all(user,action,data):
	query = ndb.gql("""SELECT * FROM Server""")
	for s in query:
		f_update(s.link,user,action,data)

# sent a request
def f_send_request(url,suffix,form_fields,method):
	form_data = urllib.urlencode(form_fields)
	result = urlfetch.fetch(url=url+'/'+suffix, 
		payload=form_data,
	    method=method,
	    headers={'Content-Type': 'application/x-www-form-urlencoded'})

	return result.content

def f_sent_all_request_and_union(suffix,form_fields,method):
	query = ndb.gql("""SELECT * FROM Server""")
	for s in query:
		pass


# Union 2 dictionary compused from lists
def f_union_dict_list(dic1,dic2):
	dic = dict()
	for name,age in dic1.items():
	    name = str(name)
	    dic[name] = list(set(dic1[name]).union(set(dic2[name])))	

	return dic	
		
app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/register',Register),
    ('/unRegister',Unregister),
    ('/blogin',UserBLogin),
    ('/login',UserLogin),
    ('/logoff',UserLogoff),
    ('/saveuserindatabase',SaveUser),
    ('/getNumOfClients',GetNumOfClients),
    ('/getMyChannels', GetMyChannels),
    ('/addChannel',AddChannel),
    ('/joinChannel',JoinChannel),
    ('/leaveChannel',LeaveChannel),
    ('/update',Update),
    ('/sendMessage',SendMessage),
    ('/redirect',Redirect),
    ('/getServers',GetServers),
    ('/getChannels',GetChannels),
    ('/getUpdates',GetUpdates),
    ('/getNetwork',getNetwork)

], debug=True)
