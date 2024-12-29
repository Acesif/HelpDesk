export class Issue {
  public tracking_number: string;
  public title: string;
  public description: string;
  public issueCategory: string;
  public status: string;
  public office: number;
  public attachments: File[];

  constructor(
    tracking_number: string,
    title: string,
    description: string,
    issueCategory: string,
    status: string,
    office: number,
    attachments: File[] = [],
  ) {
    this.tracking_number = tracking_number;
    this.title = title;
    this.description = description;
    this.issueCategory = issueCategory;
    this.status = status;
    this.office = office;
    this.attachments = attachments;
  }
}
