import { Directive, ElementRef, HostListener, Renderer2 } from '@angular/core';

@Directive({
  selector: '[appDropdown]'
})
export class DropdownDirective {
  private isOpen = false;

  constructor(private elementRef: ElementRef, private renderer: Renderer2) {}

  @HostListener('document:click', ['$event']) onDocumentClick(event: Event) {
    const dropdownElement = this.elementRef.nativeElement.querySelector('.dropdown-menu');

    if (this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen = !this.isOpen;
      this.toggleDropdown(dropdownElement, this.isOpen);
    } else if (this.isOpen) {
      this.isOpen = false;
      this.toggleDropdown(dropdownElement, false);
    }
  }

  private toggleDropdown(element: HTMLElement, isOpen: boolean): void {
    if (isOpen) {
      this.renderer.addClass(element, 'show');
    } else {
      this.renderer.removeClass(element, 'show');
    }
  }
}
