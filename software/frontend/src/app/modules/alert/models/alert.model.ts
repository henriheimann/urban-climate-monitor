export class Alert {
  destination = 'main';

  type = 'danger';

  messageKey: string;

  messageParams: any = {};

  constructor(messageKey: string, destination?: string, messageParams?: any, type?: string) {
    this.messageKey = messageKey;
    if (destination) {
      this.destination = destination;
    }
    if (messageParams) {
      this.messageParams = messageParams;
    }
    if (type) {
      this.type = type;
    }
  }
}
