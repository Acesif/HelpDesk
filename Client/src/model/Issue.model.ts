export class Issue {
  public tracking_number: string;
  public subject: string;
  public status: string;
  public office: string;
  public date: string;

  constructor(tracking_number: string, subject: string, status: string, office: string, date: string) {
    this.tracking_number = tracking_number;
    this.subject = subject;
    this.status = status;
    this.office = office;
    this.date = date;
  }
}
