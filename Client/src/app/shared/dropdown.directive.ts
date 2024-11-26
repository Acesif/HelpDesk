import {Directive, ElementRef, HostListener} from '@angular/core';

@Directive({
  selector: '[appDropdown]'
})
export class DropdownDirective {
  constructor(private elementRef: ElementRef) { }

  @HostListener('click') toggleOpen() {
    const dropDownElement = this.elementRef.nativeElement.querySelector('.dropdown-menu');

    if (dropDownElement.classList.contains('show')) {
      dropDownElement.classList.remove('show');
    } else {
      dropDownElement.classList.add('show');
    }
  }
}
