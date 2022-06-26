import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberThousand',
})
export class NumberThousandPipe implements PipeTransform {
  transform(input: any, args?: any): any {
    const suffixes = ['k', 'M', 'G', 'T', 'P', 'E'];

    if (Number.isNaN(input)) {
      return null;
    }

    if (input < 1000) {
      return input;
    }

    const exponent = Math.floor(Math.log(input) / Math.log(1000));

    return (input / Math.pow(1000, exponent)).toFixed(args) + suffixes[exponent - 1];
  }
}
