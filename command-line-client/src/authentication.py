# Created by Karol Meksuła
# 26-08-2018

from src.profile import ProfilePreferences


class AuthenticationCache:
    _instance = None

    def __init__(self):
        self.login = None
        self.password = None
        self.profile = None

        if AuthenticationCache._instance is not None:
            raise Exception("This class is exit [singleton]!")
        else:
            AuthenticationCache._instance = self

    @staticmethod
    def get_instance():
        if AuthenticationCache._instance is None:
            AuthenticationCache()
        return AuthenticationCache._instance

    def set_login(self, login: str):
        self.login = login

    def set_password(self, password: str):
        self.password = password

    def set_profile(self, profile: ProfilePreferences):
        self.profile = profile

    def info(self):
        print(self.login, self.password)
