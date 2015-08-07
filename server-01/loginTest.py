import unittest

import webapp2

import main

class HelloWorldTestCase(unittest.TestCase):

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def testThatWillWork(self):
        request = webapp2.Request.blank('/')
        response = request.get_response(main.app)
        self.assertEqual(response.status_int, 200)
        self.assertEqual(response.body, 'Hello, World!')



    if __name__ == '__main__':
        unittest.main()
