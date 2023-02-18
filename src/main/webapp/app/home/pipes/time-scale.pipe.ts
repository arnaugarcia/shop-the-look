import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timeScale',
})
export class TimeScalePipe implements PipeTransform {
  transform(milliseconds: number): string {
    if (milliseconds < 1000) {
      return `${milliseconds}ms`;
    }
    if (milliseconds < 60000) {
      return `${Math.floor(milliseconds / 1000)}s`;
    }
    if (milliseconds < 3600000) {
      return `${Math.floor(milliseconds / 60000)}m`;
    }
    if (milliseconds < 86400000) {
      return `${Math.floor(milliseconds / 3600000)}h`;
    }
    if (milliseconds < 604800000) {
      return `${Math.floor(milliseconds / 86400000)}d`;
    }
    if (milliseconds < 2419200000) {
      return `${Math.floor(milliseconds / 604800000)}w`;
    }
    return `${Math.floor(milliseconds / 2419200000)}y`;
  }
}
