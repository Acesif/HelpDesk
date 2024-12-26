export class Issue {
  public title: string;
  public description: string;
  public issueCategory: string;
  public office: string;
  public attachments: File[];

  constructor(
    title: string,
    description: string,
    office: string,
    issueCategory: string,
    attachments: File[] = [],
  ) {
    this.title = title;
    this.description = description;
    this.issueCategory = issueCategory;
    this.office = office;
    this.attachments = attachments;
  }
}
