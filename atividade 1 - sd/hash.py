import hashlib

def Sha512Hash(Password):
    HashedPassword=hashlib.sha512(Password.encode('utf-8')).hexdigest()
    print(HashedPassword)

Sha512Hash('Hi')
Sha512Hash('Hi')