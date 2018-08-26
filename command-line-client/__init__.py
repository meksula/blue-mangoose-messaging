# Created by Karol Meksuła
# 26-08-2018

import sys
from src.chat import Chat


if __name__ == '__main__':
    mode = None
    if sys.argv.__len__() > 1:
        mode = sys.argv[1]

    Chat().chat_start(mode)
