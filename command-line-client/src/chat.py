# Created by Karol Meksuła
# 26-08-2018

import requests
from src.authentication import AuthenticationCache
from sty import fg
from requests.auth import HTTPBasicAuth
import jsonpickle


def app_text(prompt: str):
    print(fg.blue, prompt, fg.rs)


class Chat:

    def __init__(self):
        self.mainCommand = None
        self.mode = None
        self.run = True
        self.auth = None
        self.login = None
        self.password = None

    def chat_start(self, mode: str):
        if mode is None:
            self.mode = "server"
        else:
            self.mode = mode

        print(fg.blue, "Hello friend in Blue Mangoose chat! <mode:" + self.mode.__str__() + ">", fg.rs)
        print(fg.blue, "(If you are new user, please type '$man')")
        self.flow()

    def flow(self):
        while self.run is True:
            self.cursor_global()
            self.base_action()

    def cursor_global(self):
        print(fg.cyan, "$", fg.rs, end="")
        self.mainCommand = input()

    def base_action(self):
        if self.mainCommand == "login":
            app_text("Log in to Blue Mangoose.\nType your username:")
            self.login = self.cursor_local()
            app_text("Now, enter your password:")
            self.password = self.cursor_local()
            self.auth = AuthenticationCache.get_instance()
            self.auth.set_login(self.login)
            self.auth.set_password(self.password)
            # TODO consider how to deserialize json! in correct way
            json = self.fetch_profile()
            profile = self.profile_create(json)
            self.auth.set_profile(profile)
            prof = self.auth.profile
            items = dict(prof).items()

        if self.mainCommand == "exit" or self.mainCommand == "close" or self.mainCommand == "quit":
            app_text("Bye!")
            quit()

    def cursor_local(self):
        print(fg.green, "$", fg.rs, end="")
        return input()

    def fetch_profile(self):
        resp = requests.get('http://localhost:8060/api/v1/profile', auth=HTTPBasicAuth(self.login, self.password))
        return resp.content

    def profile_create(self, json: str):
        return jsonpickle.decode(json)

