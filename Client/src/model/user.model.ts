export class User {
  id: number;
  name: string;
  email: string;
  phoneNumber: string;
  officeId: number;
  designation: string;
  password: string;

  constructor(id: number, name: string, email: string, phoneNumber: string, officeId: number, designation: string, password: string) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.officeId = officeId;
    this.designation = designation;
    this.password = password;
  }
}
