# Created by Karol Meksuła
# 26-08-2018


class ProfilePreferences(object):

    def __init__(self, profileId: int, userId: int, profileUsername: str, contactsBook: list, notifications: list):
        self.profileId = profileId
        self.userId = userId
        self.profileUsername = profileUsername
        self.contactsBook = contactsBook
        self.notifications = notifications

    def info(self):
        print(str(self.profileId), self.profileUsername)
