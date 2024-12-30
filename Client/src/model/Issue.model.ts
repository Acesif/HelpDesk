export class Issue {
  public id: number;
  public trackingNumber: string;
  public title: string;
  public description: string;
  public category: string;
  public status: string;
  public officeId: number;
  public postedOn: string;
  public postedBy: string;
  public updatedOn: string;
  public attachments: File[];

  constructor(
    id: number,
    trackingNumber: string,
    title: string,
    description: string,
    category: string,
    status: string,
    officeId: number,
    postedOn: string,
    postedBy: string,
    updatedOn: string,
    attachments: File[] = [],
  ) {
    this.id = id;
    this.trackingNumber = trackingNumber;
    this.title = title;
    this.description = description;
    this.category = category;
    this.status = status;
    this.officeId = officeId;
    this.postedOn = postedOn;
    this.postedBy = postedBy;
    this.updatedOn = updatedOn;
    this.attachments = attachments;
  }
}
