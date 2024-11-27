import {User} from '../../model/user.model';

export class ProfileService {
  users: User[] = [
    new User(0,"name","email@email.com","01234567987",1,"Admin","123456789"),
    new User(1,"skfjls","sdfsdf@email.com","7897987987987",2,"User","123456789"),
    new User(2,"dikeurw","complainit@email.com","12454213246",1,"Complainant","123456789"),
  ];

  getUsers(): User[] {
    return this.users;
  }

  getUserById(id: string): User {
    return this.users.find(u => (u.id).toString() === id);
  }
}
