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

from google.appengine.ext import ndb 
from google.appengine.api import users
from datetime import datetime
# ...

# Class of message

# Class Represents user in the data base
class User(ndb.Model):
	nickname = ndb.StringProperty()
	link = ndb.StringProperty()
	lastSeen = ndb.DateTimeProperty()
	#mail = ndb.StringProperty()

	
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

# Class inverts channels to JSON
class Channels(ndb.Model):
	def __init__(self, channels):
		self.channels = channels
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

# Class invert message to JSON
class R_Msg():
	def __init__(self,status,message):
		self.status = status
		self.message = message

	def to_JSON(self):
		return json.dumps(self, default=lambda o: o.__dict__,sort_keys=True, indent=4)

# Class of the register action
class Register(webapp2.RequestHandler): 
	def post(self):
		# Selects all of the servers
		query = ndb.gql("""SELECT * FROM Server""")
		# If there are more than 3 servers
		if query.count()>3:
			m = R_Msg('0','Full')
			self.response.write(m.to_JSON())
			pass
		# Less than 3 servers
		else:
			server = Server(id = self.request.get('link'),link = self.request.get('link'))
			server.put()
			m = R_Msg('1','')
			self.response.write(m.to_JSON())

			# Writing the links for debugging
			for link in query:
				self.response.write(link)
				self.response.write('\n')

# Unregister action
class Unregister(webapp2.RequestHandler):
	def post(self):
		# deleting the show from ther server table
		key = ndb.Key('Server', self.request.get('link')) 
		key.delete()
		m = R_Msg('1','')
		self.response.write(m.to_JSON())
			
# Main Handler (debugging)
class MainHandler(webapp2.RequestHandler):
    def get(self):
    	#test@example.com
    	f_signAsRead('dasd','test@example.com')
    	# user_key = ndb.Key('User', 'orwn')
    	# channel_key = ndb.Key('Channel','c')
    	# self.response.write(user_key)
    	# self.response.write(users.get_current_user())
    	# self.response.write(channel_key)
    	# address_k = ndb.Key.from_path('Message', users.get_current_user().nickname(), channel_key, False)
		#address = db.get(address_k)
		#message = Message(user = user_key,channel = channel_key,text = "bla",longtitude=5,latitude=6)
		#message.put()
    	#messageRead = MessageRead(message = message,user = users.get_current_user(),isRead = False)
		# messageRead.put()
  #   	f_signAsRead(users.get_current_user(),messageRead)
  		#self.response.write(user_key)

# UserLogin action
class UserLogin(webapp2.RequestHandler):
    def get(self):
    	# gets the current user
        user = users.get_current_user()
        if user:
            greeting = ('Welcome, %s! (<a href="%s">sign out</a>)' %
                        (user.nickname(), users.create_logout_url('/')))
        else:
        	# sign in for debuggin through browser
            greeting = ('<a href="%s">Sign in or register</a>.' %
                        users.create_login_url('/'))

        self.response.out.write('<html><body>%s</body></html>' % greeting)
        user = users.get_current_user()
        if user:
        	self.response.out.write('user')
       		f_addUser(user.nickname(),link = user.nickname())

# SaveUser action
class SaveUser(webapp2.RequestHandler):
	def get(self):
		# getting the currect user
		user = users.get_current_user()
		if user:
			# Creating user instance
			user = User(id = user.nickname(),nickname = user.nickname(),link=user.nickname())
			# Save in the user table
			user.put();
			# Responding
			m = R_Msg('1','Succeeded')
			self.response.write(m.to_JSON())
			
		else:
			# Responding
			m = R_Msg('1','User not exist')
			self.response.write(m.to_JSON())

# UserLogOf action
class UserLogof(webapp2.RequestHandler):
	def get(self):
		user = users.get_current_user()
		# If the user is in
		if user:
			# Deleting from the user table
			key = ndb.Key('User', user.nickname()) 
			key.delete()

			# Logging out
			self.redirect(users.create_logout_url(self.request.uri))
			
			# Sending response
			m = R_Msg('1','')
			self.response.write(m.to_JSON())
			
		else:
			# Responding for error
			m = R_Msg('1','User not exist')
			self.response.write(m.to_JSON())


# GetMyChannels action
class GetMyChannels(webapp2.RequestHandler):
	def get(self):
		# Select all the channels from the channel table
		query = ndb.gql("""SELECT * FROM Channel""")
		channels = []
		# For each channel adding it to a channel array
		for channel in query:
			channel.append(Channel_JSON(name = channel.name,icon = channel.icon , channel_id = channel.channel_id))

		# Convert to JSON and sent it as a responde
		chans = Channels(channels)
		self.response.out.write(chans.to_JSON())

# GetNUmOfClients action
class GetNumOfClients(webapp2.RequestHandler):
	def post(self):
		channel_id = self.request.get('id')
		# Checking if channel exists 
		
		# Selecting the chanek user when the chanel equals to the chanel name

		#self.response.out.write(key.get())
	
		# Respinding the count
		self.response.out.write(f_getNumOfClient(channel_id))

# Update action
class Update(webapp2.RequestHandler):
	def post(self):
		# Getting the user the action and the dat
		user_name = self.request.get('user')
		action = int(self.request.get('action'))
		data = self.request.get('data')
		# Writs the data (debugging)
		self.response.write(data)
		data_arr = json.loads(data)
		#self.response.write(data_arr['server'])

		# Doiung the currect action
		if(action == 1):
			f_addUser(user_name,data_arr['server'])
		elif(action == 2):
			f_delUser(user_name)
		elif(action == 3):
			f_addMessage(user_name,data_arr['channel'],data_arr['text'],data_arr['longtitude'],latitude=data_arr['latitude'])
		elif(action == 4):
			f_addChannel(data_arr['channel_id'],data_arr['name'],data_arr['icon'],False)
		
		elif(action == 5):
			f_addChannelUser(user_name,data_arr['channel_id'])
			
		elif(action == 6):
			f_delChannelUser(user_name,data_arr['channel_id'])
			
		elif(action == 7):
			f_delChannl(data_arr['channel_id'])
			
		m = R_Msg('1','')
		self.response.write(m.to_JSON())


class AddChannel(webapp2.RequestHandler):
	def post(self):
		# Getting the user the action and the dat
		name = self.request.get('name')
		channel_id = self.request.get('id')
		icon = self.request.get('icon')

		f_addChannel(channel_id,name,icon,True)

		m = R_Msg('1','')
		self.response.write(m.to_JSON())

class JoinChannel(webapp2.RequestHandler):
	def post(self):
		#pass
		user = users.get_current_user()
		# If the user is in
		if user:
			channel_id = self.request.get('id')
			channel_key = ndb.Key('Channel',channel_id)
	
			if f_isChannelExist(channel_id):
				f_addChannelUser(user.nickname(),channel_id)
				m = R_Msg('1','')
			else:
				m = R_Msg('0','Channel does not exist')

		else:
			m = R_Msg('0','User is disconnected!')
		
		self.response.write(m.to_JSON())

class SendMessage(webapp2.RequestHandler):
	def post(self):
		# Getting String channel_id, String text, Double longtitude, Double latitude
		channel_id = self.request.get('channel_id')
		text = self.request.get('text')
		longtitude = self.request.get('longtitude')
		latitude = self.request.get('latitude')

		user = users.get_current_user()
		# If the user is in
		if user:
			if f_isChannelExist(channel_id):
				f_addMessage(user.nickname(),channel_id,text,longtitude,latitude)

				m = R_Msg('1','')
			else:
				m = R_Msg('0','Channel does not exist')
		else:
			m = R_Msg('0','User is disconnected!')

		self.response.write(m.to_JSON())


class LeaveChannel(webapp2.RequestHandler):
	def post(self):
		channel_id = self.request.get('id')
		user = users.get_current_user()
		if user:
			if f_isChannelExist(channel_id):
				if f_isChannelUserExist(channel_id,user.nickname()):
					f_delChannelUser(user.nickname(),channel_id)
					if f_getNumOfClient == 0:
						f_delChannl(channel_id)
					m = R_Msg('1','')

				else:
					m = R_Msg('0','User does not belong to channel')

					
			else:
				m = R_Msg('0','Channel does not exist')
		else:
			m = R_Msg('0','User is disconnected!')

		self.response.write(m.to_JSON())



# function for adding data for table

# Add user
def f_addUser(user_name,link):
	user = User(id = user_name,nickname = user_name,link = link,lastSeen = datetime.now())
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
	channel_key.delete()

# 
def f_isChannelUserExist(channel_id,user_name):
	query = ndb.gql("""SELECT * FROM ChannelUser""")
	count = 0
	for channelUser in query:
		if channelUser.channel.get().channel_id == chan_id and channelUser.user.get().name == user_name:
			count = count+1

def f_isChannelExist(channel_id):
	query = ndb.gql("""SELECT * FROM Channel WHERE channel_id = :channel_id """,channel_id = channel_id)
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


		
app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/register',Register),
    ('/unRegister',Unregister),
    ('/login',UserLogin),
    ('/logoff',UserLogof),
    ('/saveuserindatabase',SaveUser),
    ('/getNumOfClients',GetNumOfClients),
    ('/getMyChannels', GetMyChannels),
    ('/addChannel',AddChannel),
    ('/joinChannel',JoinChannel),
    ('/leaveChannel',LeaveChannel),
    ('/update',Update),
    ('/sendMessage',SendMessage)

], debug=True)
