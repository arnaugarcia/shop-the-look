import { Directive, ElementRef, Input, Inject, ChangeDetectorRef, OnChanges, SimpleChanges } from '@angular/core';

import * as Feather from 'feather-icons';

@Directive({
  selector: '[stlDataFeather]',
})
export class FeatherIconDirective implements OnChanges {
  // Private
  private _nativeElement: any;

  @Input() private name!: string;
  @Input() private class!: string;
  @Input() private size!: string;
  @Input() private inner!: boolean;

  /**
   * Constructor
   *
   * @param {ElementRef} _elementRef
   */
  constructor(@Inject(ElementRef) private _elementRef: ElementRef, @Inject(ChangeDetectorRef) private _changeDetector: ChangeDetectorRef) {}

  ngOnChanges(changes: SimpleChanges): void {
    // Get the native element
    this._nativeElement = this._elementRef.nativeElement;

    // SVG parameter
    this.name = changes.name.currentValue;
    this.size = changes.size.currentValue; // Set default size 14
    this.class = changes.class.currentValue;

    // Create SVG
    const svg = Feather.icons[this.name].toSvg({
      class: this.class,
      width: this.size,
      height: this.size,
    });

    // Set SVG
    if (this.inner) {
      this._nativeElement.innerHTML = svg;
    } else {
      this._nativeElement.outerHTML = svg;
    }
    this._changeDetector.markForCheck();
  }
}
