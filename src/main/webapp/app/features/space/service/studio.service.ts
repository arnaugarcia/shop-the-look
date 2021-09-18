import { EventEmitter, Injectable } from '@angular/core';
import { EmptySpace, ISpace } from '../model/space.model';

type CurrentStep = 'template' | 'create' | 'customize' | 'enjoy' | 'publish';

export enum StudioTemplate {
  ONE_PHOTO = 'ONE_PHOTO',
  THREE_PHOTOS_VERTICAL = 'THREE_PHOTOS_VERTICAL',
  THREE_PHOTOS_HORIZONTAL = 'THREE_PHOTOS_HORIZONTAL',
}

interface IStudioData {
  template: StudioTemplate;
  space: ISpace;
}

@Injectable({
  providedIn: 'root',
})
export class StudioService {
  currentStep: CurrentStep = 'create';
  navigation: EventEmitter<string>;
  data: IStudioData;

  constructor() {
    this.navigation = new EventEmitter<string>();
    this.data = {
      template: StudioTemplate.ONE_PHOTO,
      space: new EmptySpace(),
    };
  }

  navigate(step: CurrentStep): void {
    this.currentStep = step;
    this.navigation.emit(step);
  }

  get template(): StudioTemplate {
    return this.data.template;
  }

  set template(template: StudioTemplate) {
    this.data.template = template;
  }

  /*  set name(name: string) {
      this.data.space.name = name;
    }

    set description(description: string) {
      this.data.space.description = description;
    }

    addPhoto(photo: IPhoto): void {
      this.space.photos.push(photo);
    }

    removePhoto(photo: IPhoto): void {
      const index = this.space.photos.indexOf(photo);
      this.space.photos.slice(index, 1);
    }

    addCoordinate(coordinate: ICoordinate, photo: IPhoto): void {
      const index = this.space.photos.indexOf(photo);
      if (index > -1) {
        const coordinates = this.space.photos[index].coordinates;
        if (coordinates) {
          coordinates.push(coordinate);
        }
      }
    }

    removeCoordinate(coordinate: ICoordinate, photo: IPhoto): void {
      const index = this.space.photos.indexOf(photo);
      if (index > -1) {
        const coordinates = this.space.photos[index].coordinates;
        if (coordinates) {

        }
      }
    }

    get space(): ISpace {
      return this.data.space;
    }*/
}
